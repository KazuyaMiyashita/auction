case class Account(id: Int) // TODO PASSWORD
case class Auction(id: Int, name: String, minAmount: Int, maxAmount: Int, bids: List[Bid], isOpen: Boolean)
case class Bid(nyuusatsushaId: Int, auctionId: Int, amount: Int)
