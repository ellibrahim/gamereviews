package com.example.gamereviews.com.example.gamereviews

import androidx.annotation.DrawableRes
import com.example.gamereviews.R

data class Game(
    val title: String,
    @DrawableRes val imageResId: Int,
    val releaseDate: String,
    val developer: String,
    val publisher: String,
    val description: String
)

// Define game details here
val gameDetails = mapOf(
    "The Witcher 3" to Game("The Witcher 3", R.drawable.witcher3, "May 19, 2015", "CD Projekt Red", "CD Projekt", "An open-world action RPG."),
    "Red Dead Redemption 2" to Game("Red Dead Redemption 2", R.drawable.rdr2, "October 26, 2018", "Rockstar Games", "Rockstar Games", "An epic tale of life in America’s unforgiving heartland."),
    "Cyberpunk 2077" to Game("Cyberpunk 2077", R.drawable.cyberpunk2077, "December 10, 2020", "CD Projekt Red", "CD Projekt", "An open-world RPG set in the futuristic Night City."),
    "Elden Ring" to Game("Elden Ring", R.drawable.eldenring, "February 25, 2022", "FromSoftware", "Bandai Namco", "A vast open-world RPG with deep lore and challenging gameplay."),
    "Dark Souls 3" to Game("Dark Souls 3", R.drawable.darksouls3, "March 24, 2016", "FromSoftware", "Bandai Namco", "A dark fantasy action RPG with punishing difficulty."),
    "God of War Ragnarok" to Game("God of War Ragnarok", R.drawable.godofwarragnarok, "November 9, 2022", "Santa Monica Studio", "Sony Interactive Entertainment", "Kratos and Atreus face the end of the world in Norse mythology."),
    "Horizon Forbidden West" to Game("Horizon Forbidden West", R.drawable.horizonfw, "February 18, 2022", "Guerrilla Games", "Sony Interactive Entertainment", "Aloy continues her journey in a lush post-apocalyptic world."),
    "The Last of Us Part II" to Game("The Last of Us Part II", R.drawable.tlou2, "June 19, 2020", "Naughty Dog", "Sony Interactive Entertainment", "A deeply emotional post-apocalyptic narrative-driven game."),
    "Ghost of Tsushima" to Game("Ghost of Tsushima", R.drawable.ghostoftsushima, "July 17, 2020", "Sucker Punch Productions", "Sony Interactive Entertainment", "A samurai adventure set in feudal Japan."),
    "Bloodborne" to Game("Bloodborne", R.drawable.bloodborne, "March 24, 2015", "FromSoftware", "Sony Computer Entertainment", "A gothic horror action RPG with Lovecraftian themes."),
    "Sekiro: Shadows Die Twice" to Game("Sekiro: Shadows Die Twice", R.drawable.sekiro, "March 22, 2019", "FromSoftware", "Activision", "A fast-paced samurai action game with challenging combat."),
    "Assassin's Creed Valhalla" to Game("Assassin's Creed Valhalla", R.drawable.acvalhalla, "November 10, 2020", "Ubisoft Montreal", "Ubisoft", "Lead Viking raids and build your settlement in medieval England."),
    "Far Cry 6" to Game("Far Cry 6", R.drawable.farcry6, "October 7, 2021", "Ubisoft Toronto", "Ubisoft", "Fight against a ruthless dictator in a tropical open world."),
    "Resident Evil Village" to Game("Resident Evil Village", R.drawable.re8, "May 7, 2021", "Capcom", "Capcom", "Survival horror game featuring vampires and werewolves."),
    "Doom Eternal" to Game("Doom Eternal", R.drawable.doometernal, "March 20, 2020", "id Software", "Bethesda Softworks", "Fast-paced demon-slaying first-person shooter."),
    "Metro Exodus" to Game("Metro Exodus", R.drawable.metroexodus, "February 15, 2019", "4A Games", "Deep Silver", "Survive in a post-apocalyptic Russia filled with dangers."),
    "Final Fantasy VII Remake" to Game("Final Fantasy VII Remake", R.drawable.ff7remake, "April 10, 2020", "Square Enix", "Square Enix", "A reimagining of the classic RPG with modern graphics and gameplay."),
    "Persona 5 Royal" to Game("Persona 5 Royal", R.drawable.p5r, "October 31, 2019", "Atlus", "Atlus", "A stylish RPG about high school students changing the world."),
    "Monster Hunter: World" to Game("Monster Hunter: World", R.drawable.mhw, "January 26, 2018", "Capcom", "Capcom", "Hunt massive creatures in a vibrant ecosystem."),
    "Death Stranding" to Game("Death Stranding", R.drawable.deathstranding, "November 8, 2019", "Kojima Productions", "Sony Interactive Entertainment", "A unique open-world adventure about reconnecting America."),
    "Marvel’s Spider-Man" to Game("Marvel’s Spider-Man", R.drawable.spiderman, "September 7, 2018", "Insomniac Games", "Sony Interactive Entertainment", "Swing through New York City as the iconic superhero."),
    "Halo Infinite" to Game("Halo Infinite", R.drawable.haloinfinite, "December 8, 2021", "343 Industries", "Xbox Game Studios", "A new chapter in Master Chief’s legendary journey."),
    "Gears 5" to Game("Gears 5", R.drawable.gears5, "September 10, 2019", "The Coalition", "Xbox Game Studios", "A third-person shooter featuring intense squad-based combat.")
)
