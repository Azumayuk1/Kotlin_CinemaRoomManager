package cinema

class CinemaRoom(
    _numberOfRows: Int = 5,
    _seatsInRow: Int = 10,
    private var frontTicketPrice: Int = 10,
    private var backTicketPrice: Int = 8,
    totalSeatsToConsiderCinemaBig: Int = 60,
) {
    // Check that numbers of rows and seats are > 0
    private var numberOfRows: Int = 1
    private var seatsInRow: Int = 1
    init {
        if(_numberOfRows < 1 || _seatsInRow < 1) {
            println("Number of rows and/or seats is too small. Set to default - 1, 1.")
        } else {
            numberOfRows = _numberOfRows
            seatsInRow = _seatsInRow
        }
    }

    // Calculating ticket price and total number of rows
    private val totalSeats = numberOfRows * seatsInRow
    private var numberOfPurchasedTickets = 0
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

    fun changeCinemaSize() {
        println("Enter new number of rows:")
        val newNumberOfRows = readln().toInt()
        println("Enter new number of seats in row:")
        val newNumberOfSeatsPerRow = readln().toInt()

        if(newNumberOfRows < 1 || newNumberOfSeatsPerRow < 1) {
            println("Number of rows and/or seats is too small. Must be more than 1, 1.")
        } else {
            numberOfRows = newNumberOfRows
            seatsInRow = newNumberOfSeatsPerRow
            println("New cinema room size: $numberOfRows rows, $seatsInRow seats per row.")
        }
    }

    fun changeTicketPrices() {
        var newBackTicketPrice: Int = -1
        var newFrontTicketPrice: Int = -1
        try {
            println("Enter new back ticket price:")
            newBackTicketPrice = readln().toInt()
            println("Enter new front ticket price:")
            newFrontTicketPrice = readln().toInt()
        } catch (e: NumberFormatException) {
            println("Not a number.")
            return
        } finally {
            if (newBackTicketPrice < 0 || newFrontTicketPrice < 0) {
                println("Incorrect input (price can't be negative.)")
            } else {
                backTicketPrice = newBackTicketPrice
                frontTicketPrice = newFrontTicketPrice
                println("New ticket prices: front -\$$frontTicketPrice, back - \$$backTicketPrice")
            }
        }
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
               |// Administrator:
               |3. Statistics
               |4. Change cinema room size
               |5. Change ticket prices
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

    // Creating a cinema (with default args)
    val cinema = CinemaRoom(inputNumberOfRows, inputSeatsInRow)

    // Program cycle
    while (true) {
        cinema.printUserMenu()

        when (try { readln().toInt() } catch (e: NumberFormatException) { 1 }) {
            0 -> break
            1 -> cinema.printCinemaScheme()
            2 -> cinema.userBuyATicket()
            3 -> cinema.showStatistics()
            4 -> cinema.changeCinemaSize()
            5 -> cinema.changeTicketPrices()
            else -> println("No such option exits!")
        }
    }
}