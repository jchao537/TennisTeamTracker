package com.example.tennisteamtracker

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tennisteamtracker.database.*
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var playerDao: PlayerDatabaseDao
    private lateinit var gameDao: GameDatabaseDao
    private lateinit var gameDayDao: GameDayDatabaseDao
    private lateinit var db: EntityDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, EntityDatabase::class.java).build()
        playerDao = db.playerDatabaseDao
        gameDao = db.gameDatabaseDao
        gameDayDao = db.gameDayDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testPlayers() {
        assertEquals(playerDao.getNumPlayers(), 0)
        playerDao.insertPlayer(Player(playerName = "Joe", playerRank = 1, playerAge = 18))
        var lastPlayer = playerDao.getLastPlayer()
        assertEquals(lastPlayer?.playerName, "Joe")
        assertEquals(playerDao.getNumPlayers(), 1)
        playerDao.insertPlayer(Player(playerName = "bob", playerRank = 2, playerAge = 17))
        assertEquals(playerDao.getNumPlayers(), 2)
        playerDao.clearAllPlayers()
        assertEquals(playerDao.getNumPlayers(), 0)
    }

    @Test
    @Throws(Exception::class)
    fun testPlayersWithGames() {
        assertEquals(playerDao.getNumPlayers(), 0)
        playerDao.insertPlayer(Player(playerName = "Joe", playerRank = 1, playerAge = 18))
        var players = playerDao.getPlayersWithGames()
        assertEquals(players.size, 1)
        assertEquals(players[0].player.playerName, "Joe")
        assertEquals(players[0].games, emptyList<Game>())

        var joeId = players[0].player.playerId
        gameDao.insertGame(Game(gamePlayerId = joeId, opponentName = "Bob", ownScore = 6, opponentScore = 3, isAWin = true))
        players = playerDao.getPlayersWithGames()
        assertEquals(players.size, 1)
        assertEquals(players[0].player.playerName, "Joe")
        assertEquals(players[0].games?.size, 1)
        assertEquals(players[0].games?.get(0)?.opponentName, "Bob")
        assertEquals(players[0].games?.getOrNull(1)?.opponentName, null)

        gameDao.insertGame(Game(gamePlayerId = joeId, opponentName = "Tim", ownScore = 4, opponentScore = 6, isAWin = false))
        players = playerDao.getPlayersWithGames()
        assertEquals(players.size, 1)
        assertEquals(players[0].player.playerName, "Joe")
        assertEquals(players[0].games?.size, 2)
        assertEquals(players[0].games?.get(0)?.opponentName, "Bob")
        assertEquals(players[0].games?.getOrNull(1)?.opponentName, "Tim")
        assertEquals(players[0].games?.getOrNull(2)?.opponentName, null)

        playerDao.insertPlayer(Player(playerName = "Sam", playerRank = 2, playerAge = 17))
        players = playerDao.getPlayersWithGames()
        var samId = players[1].player.playerId
        gameDao.insertGame(Game(gamePlayerId = samId, opponentName = "Jim", ownScore = 2, opponentScore = 6, isAWin = false))
        players = playerDao.getPlayersWithGames()
        assertEquals(players.size, 2)
        assertEquals(players[1].player.playerName, "Sam")
        assertEquals(players[0].games?.size, 2)
        assertEquals(players[1].games?.size, 1)
        assertEquals(players[1].games?.getOrNull(0)?.opponentName, "Jim")
        assertEquals(players[1].games?.getOrNull(1)?.opponentName, null)
    }

    @Test
    @Throws(Exception::class)
    fun testAll() {
        var player1 = Player(playerName = "Joe", playerRank = 1, playerAge = 18)
        playerDao.insertPlayer(player1)
        var players = playerDao.getPlayersWithGames()
        assertEquals(player1.playerId, 0)
        assertEquals(players[0].player.playerName, "Joe")
        var p1Id = players[0].player.playerId
        var game1 = Game(gamePlayerId = p1Id, opponentName = "Bob", ownScore = 6, opponentScore = 3, isAWin = true)
        var game2 = Game(gamePlayerId = p1Id, opponentName = "Tim", ownScore = 4, opponentScore = 6, isAWin = false)
        gameDao.insertGame(game1)
        gameDao.insertGame(game2)
        players = playerDao.getPlayersWithGames()
        assertEquals(players.size, 1)
        assertEquals(players[0].player.playerName, "Joe")
        assertEquals(players[0].games?.size, 2)
        assertEquals(players[0].games?.get(0)?.opponentName, "Bob")
        assertEquals(players[0].games?.get(0)?.ownScore, 6)
        assertEquals(players[0].games?.get(0)?.opponentScore, 3)
        assertEquals(players[0].games?.get(1)?.opponentName, "Tim")
        assertEquals(players[0].games?.get(1)?.ownScore, 4)
        assertEquals(players[0].games?.get(1)?.opponentScore, 6)
        assertEquals(players[0].games?.getOrNull(2), null)
        assertEquals(players.getOrNull(1), null)
    }

    @Test
    @Throws(Exception::class)
    fun testGameDay() {
        var gameDay1 = GameDay(opponentTeam = "Folsom")
        gameDayDao.insertGameDay(gameDay1)
        var gameDays = gameDayDao.getGameDayWithGames()
        assertEquals(gameDay1.gameDayId, 0)
        assertEquals(gameDays[0].gameDay.opponentTeam, "Folsom")

        var player1 = Player(playerName = "Joe", playerRank = 1, playerAge = 18)
        playerDao.insertPlayer(player1)
        var players = playerDao.getPlayersWithGames()
        assertEquals(player1.playerId, 0)
        assertEquals(players[0].player.playerName, "Joe")

        var p1Id = players[0].player.playerId
        var gd1Id = gameDays[0].gameDay.gameDayId
        var game1 = Game(gamePlayerId = p1Id, partOfGameDayId = gd1Id, opponentName = "Bob", ownScore = 6, opponentScore = 3, isAWin = true)
        var game2 = Game(gamePlayerId = p1Id, partOfGameDayId = gd1Id, opponentName = "Tim", ownScore = 4, opponentScore = 6, isAWin = false)
        gameDao.insertGame(game1)
        gameDao.insertGame(game2)
        players = playerDao.getPlayersWithGames()
        assertEquals(players.size, 1)
        assertEquals(players[0].player.playerName, "Joe")
        assertEquals(players[0].games?.size, 2)
        assertEquals(players[0].games?.get(0)?.opponentName, "Bob")
        assertEquals(players[0].games?.get(0)?.ownScore, 6)
        assertEquals(players[0].games?.get(0)?.opponentScore, 3)
        assertEquals(players[0].games?.get(1)?.opponentName, "Tim")
        assertEquals(players[0].games?.get(1)?.ownScore, 4)
        assertEquals(players[0].games?.get(1)?.opponentScore, 6)
        assertEquals(players[0].games?.getOrNull(2), null)
        assertEquals(players.getOrNull(1), null)
        gameDays = gameDayDao.getGameDayWithGames()
        assertEquals(gameDays.size, 1)
        assertEquals(gameDays[0].gameDay.opponentTeam, "Folsom")
        assertEquals(gameDays[0].games?.size, 2)
        assertEquals(gameDays[0].games?.get(0)?.opponentName, "Bob")
        assertEquals(gameDays[0].games?.get(0)?.ownScore, 6)
        assertEquals(gameDays[0].games?.get(0)?.opponentScore, 3)
        assertEquals(gameDays[0].games?.get(1)?.opponentName, "Tim")
        assertEquals(gameDays[0].games?.get(1)?.ownScore, 4)
        assertEquals(gameDays[0].games?.get(1)?.opponentScore, 6)
        assertEquals(gameDays[0].games?.getOrNull(2), null)
        assertEquals(gameDays.getOrNull(1), null)
    }
}