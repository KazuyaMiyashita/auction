

class Cli(application: Application) {

  def run(): Unit = {
    val accountsInfo = application.fetchAuctionInfo().mkString
    println(accountsInfo)

    println("オークションIDと金額を次の形式で指定してください: ex. 1 1500")
    val Array(id, amount) = io.StdIn.readLine().split(" ", 2).map(_.toInt)
    val resultStr = application.nyusatsu(1, amount, id)
    println(resultStr)

    run()
  }

}
