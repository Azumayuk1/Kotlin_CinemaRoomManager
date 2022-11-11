package cinema

class CinemaRoom(
    //TODO: Check that numbers of rows and seats are > 0
    private val numberOfRows: Int = 5,
    private val seatsInRow: Int = 10,
    private var frontTicketPrice: Int = 10,
    private var backTicketPrice: Int = 8,
    private val totalSeatsToConsiderCinemaBig: Int = 60,
) {
    // Calculating ticket price and total number of rows
    private val totalSeats = numberOfRows * seatsInRow
    private var numberOfPurchasedTickets = 0
    private var incomeIfAllTicketsInRoomAreSold = 0
    private var currentIncomeFromSoldTickets = 0

    init {
        backTicketPrice = if (totalSeats <= totalSeatsToConsiderCinemaBig) {
            frontTicketPrice
        } else {
            8
        }
    }

    // Creating a 2D list that represents the cinema
    private val schemeCinema = MutableList(numberOfRows) {
        "S".repeat(seatsInRow).toMutableList()
    }

    fun printCinemaScheme() {
        val numberOfRows = schemeCinema.lastIndex + 1
        val seatsInRow = schemeCinema[0].lastIndex + 1
        println("Cinema: ")

        //Displays seat numbers on top
        print("  ")
        for (i in 1..seatsInRow) {
            print("$i ")
        }

        for (row in 0 until numberOfRows) {
            print("\n${row + 1} ") //Display row number on the side
            for (seat in 0 until seatsInRow) { //print each seat in row
                print("${schemeCinema[row][seat]} ")
            }
        }
        println()
    }

    fun userBuyATicket(
    ) {
        while (true) {
            // User selects a seat, entering row and seat in row
            println("Enter a row number:")
            val userSelectedRowNumber = readln().toInt()
            println("Enter a seat number in that row:")
            val userSelectedSeatNumber = readln().toInt()

            // Minus 1 to make working with list easier
            val selectedRowNumber = userSelectedRowNumber - 1
            val selectedSeatNumber = userSelectedSeatNumber - 1

            try {
                if (schemeCinema[selectedRowNumber][selectedSeatNumber] == 'B') {
                    println("That ticket has already been purchased!")
                } else {
                    // Change seat status to 'B' - bought
                    schemeCinema[selectedRowNumber][selectedSeatNumber] = 'B'

                    // Check if the seat is in front or in the back and calculate ticket price
                    val selectedSeatTicketPrice: Int =
                        if (userSelectedRowNumber < (numberOfRows - (numberOfRows / 2))) {
                            frontTicketPrice
                        } else {
                            backTicketPrice
                        }

                    // Print ticket price, break buying cycle
                    println("Ticket price: \$$selectedSeatTicketPrice")
                    numberOfPurchasedTickets++
                    currentIncomeFromSoldTickets += selectedSeatTicketPrice
                    return
                }
            } catch (e: Exception) {
                println("Wrong input!")
            }

        }
    }

    // Counts the total income from room, if all tickets will be sold
    private fun countTotalIncome(): Int {
        var countedTotalIncome = 0

        // If cinema room is small
        if (frontTicketPrice == backTicketPrice) {
            return totalSeats * frontTicketPrice
        }

        // Counting for back and front seats
        countedTotalIncome += ((numberOfRows - 1 - (numberOfRows / 2)) * seatsInRow) * frontTicketPrice
        countedTotalIncome += (((numberOfRows / 2) + 1) * seatsInRow) * backTicketPrice

        return countedTotalIncome
    }

    fun showStatistics() {
        // Count percentage of taken seats
        val percentage: Double =
            try {
                numberOfPurchasedTickets * 100.0 / totalSeats
            } catch (e: ArithmeticException) {
                0.0
            }

        println(
            """
               |Number of purchased tickets: $numberOfPurchasedTickets
               |Percentage: ${"%.2f".format(percentage)}%
               |Current income: $$currentIncomeFromSoldTickets
               |Total income: $${countTotalIncome()}
               """.trimMargin()
        )
    }

    fun printUserMenu() {
        println(
            """
               |1. Show the seats
               |2. Buy a ticket
               |3. Statistics
               |0. Exit
               """.trimMargin()
        )
    }
}


fun main() {
    // Input cinema size
    println("Enter the number of rows:")
    val inputNumberOfRows = readln().toInt()
    println("Enter the number of seats in each row:")
    val inputSeatsInRow = readln().toInt()

    // Input cinema ticket prices
    val inputFrontTicketPrice = 10
    val inputBackTicketPrice = 8

    // Creating a cinema (with default args)
    val cinema = CinemaRoom(inputNumberOfRows, inputSeatsInRow)

    // Program cycle
    while (true) {
        cinema.printUserMenu()

        when (try {
            readln().toInt()
        } catch (e: NumberFormatException) {
            1
        }) {
            0 -> break
            1 -> cinema.printCinemaScheme()
            2 -> cinema.userBuyATicket()
            3 -> cinema.showStatistics()
        }
    }
}