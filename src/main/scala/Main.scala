
object Main extends App {

  val accountRepository = new AccountRepositoryInMemory
  val auctionRepository = new AuctionRepositoryInMemory
  val applicaiton: Application = new ApplicationImpl(accountRepository, auctionRepository)

  val account = Account(1)
  accountRepository.store(account)

  val auction = Auction(1, "ベイブレード", 1000, 5000, Nil, true)
  auctionRepository.store(auction)

  val cli = new Cli(applicaiton)

  cli.run()

}

