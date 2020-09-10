package com.example.tennisteamtracker.database

import androidx.room.*

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true) val playerId: Long = 0L,
    val playerName: String = "",
    val playerAge: Int = 0,
    val playerRank: Int = 0,
    val playerWins: Int = 0,
    val playerLosses: Int = 0,
    val playerWinPercentage: Double = 0.0
)

@Entity
data class GameDay(
    @PrimaryKey(autoGenerate = true) val gameDayId: Long = 0L,
    val opponentTeam: String = "",
    val teamWins: Int = 0,
    val teamLosses: Int = 0,
    val isATeamWin: Boolean? = null
)

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true) val gameId: Long = 0L,
    val gamePlayerId: Long = 0L,
    val partOfGameDayId: Long = 0L,
    val ownScore: Int = 0,
    val opponentScore: Int = 0,
    val isAWin: Boolean? = null
)

data class PlayersWithGames(
    @Embedded val player: Player,
    @Relation(
        parentColumn = "playerId",
        entityColumn = "gamePlayerId"
    )
    val games: List<Game>?
)

data class GameDayWithGames(
    @Embedded val gameDay: GameDay,
    @Relation(
        parentColumn = "gameDayId",
        entityColumn = "partOfGameDayId"
    )
    val games: List<Game>?
)