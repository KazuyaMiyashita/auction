/*
# 一時間でCLIアプリを作ろう!(オークション)
## 要件
オークションサイトを作ろう！
オークションの出品者は品物の名前と最低金額、最大金額を指定します。
オークションを入札する人は、その品物に対して現在の入札金額より大きい金額を指定すると、落札の候補者になります。
入札の時に最大金額以上の金額を指定した場合は、その瞬間に落札が可能です。
オークションの出品者は適当なタイミングで入札を終了することが出来ます。
その時に誰が落札できるか、または誰も落札出来ないかを出力してください。
出品者や入札者はアカウントの作成やログインが出来ます。
## 実装上の要件
アプリケーションを起動したらREPLが開いて、複数人がオークションサイトにアクセスする挙動を模倣する。
(DBによる永続化は行っても行わなくても良い)
異常な入力値の場合でもアプリケーションが落ちないようにエラーハンドリングする。
## その他の条件
1時間で実装する
必ずしも完成させなくても良いが、新卒の人のためになることを目標にしましょう
*/

trait Application {

  // return auctionId
  def newAuction(name: Int, minAmount: Int, maxAmount: Int): Int

  def fetchAuctionInfo(): List[String]

  def nyusatsu(nyuusatsushaId: Int, amount: Int, auctionId: Int): String

  def newAccount(): Int // accountId
  def login(accountId: Int): Boolean // TODO PASSWORD

}

class ApplicationImpl(
  accountRepository: AccountRepository,
  auctionRepository: AuctionRepository
) extends Application {

  override def newAuction(name: Int, minAmount: Int, maxAmount: Int): Int = ???

  override def fetchAuctionInfo(): List[String] = {
    val openAuctions = auctionRepository.list().filter(_.isOpen)
    if (openAuctions.isEmpty) {
      List("オークションがありません")
    } else {
          openAuctions.map { auction =>
      s"オークションID: ${auction.id}, 名前: ${auction.name}, 最低入札額: ${auction.minAmount}, 最大金額: ${auction.maxAmount}, 現在の入札額: ${auction.bids.headOption.map(_.amount.toString).getOrElse("入札無し")}"
    }
    }
  }

  override def nyusatsu(nyuusatsushaId: Int, amount: Int, auctionId: Int): String = {
    val result: Either[String, String] = for {
      nyuusatsusha <- accountRepository.fetchBy(nyuusatsushaId).toRight("アカウントがありません")
      auction <- auctionRepository.fetchBy(auctionId).toRight("オークションが見つかりません")
      _ <- {
        if (!auction.isOpen) Left("オークションは終了してます")
        else if (amount < auction.minAmount) Left("入札額が最低金額を下回っています")
        else auction.bids.headOption match {
          case None => Right(())
          case Some(lastBid) => if (amount < lastBid.amount) Left("入札額が現在の金額を下回っています") else Right(())
        }
      }
    } yield {
      if (amount >=  auction.maxAmount) {
        val newBid = Bid(nyuusatsusha.id, auction.id, amount)
        val nextAuction = auction.copy(bids = newBid :: auction.bids, isOpen = false)
        auctionRepository.store(nextAuction)
        "落札しました！"
      } else {
        val newBid = Bid(nyuusatsusha.id, auction.id, amount)
        val nextAuction = auction.copy(bids = newBid :: auction.bids)
        auctionRepository.store(nextAuction)
        "入札しました"
      }
    }
    result.merge
  }


  override def newAccount(): Int = ???
  override def login(accountId: Int): Boolean = ???
  
}

