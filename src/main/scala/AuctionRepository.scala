trait AuctionRepository {
  def fetchBy(auctionId: Int): Option[Auction]
  def list(): List[Auction]
  def store(auction: Auction): Boolean
}

class AuctionRepositoryInMemory extends AuctionRepository {

  // AuctionId, Auction
  private val auctions: collection.mutable.HashMap[Int, Auction] = 
    collection.mutable.HashMap[Int, Auction]()

  override def fetchBy(auctionId: Int): Option[Auction] = {
    auctions.get(auctionId)
  }

  override def list(): List[Auction] = auctions.values.toList

  // 成功したらtrur
  override def store(auction: Auction): Boolean = {
    auctions.put(auction.id, auction).isDefined
  }

}
