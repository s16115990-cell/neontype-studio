package com.example.domain.engine

import com.example.domain.model.BioTemplate
import com.example.domain.model.CreatorPlatform
import com.example.domain.model.StickerCategory
import com.example.domain.model.StickerItem

object StickerAndTemplateCatalog {

    val stickers: List<StickerItem> = listOf(
        // 1. Skull & Skeleton (কঙ্কাল)
        StickerItem("sk1", StickerCategory.SKULL, "Classic Skull", "💀", listOf("skull", "কঙ্কাল", "dark")),
        StickerItem("sk2", StickerCategory.SKULL, "Crossbones", "☠️", listOf("danger", "pirate", "skull")),
        StickerItem("sk3", StickerCategory.SKULL, "X-Ray Skeleton", "🩻", listOf("skeleton", "bones", "xray")),
        StickerItem("sk4", StickerCategory.SKULL, "Bone Relic", "🦴", listOf("bone", "skeleton")),
        StickerItem("sk5", StickerCategory.SKULL, "Dark Coffin", "⚰️", listOf("coffin", "rip", "edit")),
        StickerItem("sk6", StickerCategory.SKULL, "Headstone", "🪦", listOf("grave", "rip")),
        StickerItem("sk7", StickerCategory.SKULL, "Ghost Edit", "👻", listOf("ghost", "spooky")),
        StickerItem("sk8", StickerCategory.SKULL, "Demon Mask", "👹", listOf("oni", "demon", "dark")),
        StickerItem("sk9", StickerCategory.SKULL, "Smiling Devil", "😈", listOf("devil", "attitude")),
        StickerItem("sk10", StickerCategory.SKULL, "Skull Clan", "꧁💀༒☬🔥☬༒💀꧂", listOf("clan", "gaming", "skull")),
        StickerItem("sk11", StickerCategory.SKULL, "Dark Chains", "⛓️💀⛓️", listOf("chain", "dark", "edit")),
        StickerItem("sk12", StickerCategory.SKULL, "Blood Dagger", "🩸🗡️💀", listOf("blade", "sigma", "edit")),
        StickerItem("sk13", StickerCategory.SKULL, "Reaper Frame", "◤💀✞☠️✞💀◥", listOf("reaper", "frame")),
        StickerItem("sk14", StickerCategory.SKULL, "Black Heart Skull", "🖤💀☠️🖤", listOf("black", "skull")),

        // 2. Sad & Heartbreak / Bewafa (বেবাফা)
        StickerItem("bw1", StickerCategory.BEWAFA, "Broken Heart", "💔", listOf("sad", "bewafa", "বেবাফা")),
        StickerItem("bw2", StickerCategory.BEWAFA, "Wilted Rose", "🥀", listOf("rose", "sad", "heartbreak")),
        StickerItem("bw3", StickerCategory.BEWAFA, "Black Soul", "🖤", listOf("dark", "love", "sad")),
        StickerItem("bw4", StickerCategory.BEWAFA, "Crying Tears", "😭", listOf("cry", "sad")),
        StickerItem("bw5", StickerCategory.BEWAFA, "Falling Tear", "💧", listOf("tear", "rain", "sad")),
        StickerItem("bw6", StickerCategory.BEWAFA, "Rainy Mood", "🌧️", listOf("rain", "lofi", "sad")),
        StickerItem("bw7", StickerCategory.BEWAFA, "Bandaged Heart", "🩹🫀", listOf("healing", "heart")),
        StickerItem("bw8", StickerCategory.BEWAFA, "Lonely Candle", "🕯️🥀", listOf("candle", "miss")),
        StickerItem("bw9", StickerCategory.BEWAFA, "Bewafa Combo", "💔🥀🖤💧", listOf("bewafa", "বেবাফা", "status")),
        StickerItem("bw10", StickerCategory.BEWAFA, "Sad Divider", "━━💔🥀💔━━", listOf("divider", "sad")),
        StickerItem("bw11", StickerCategory.BEWAFA, "Torn Memory", "❥━──💔🥀──━❥", listOf("memory", "love", "sad")),
        StickerItem("bw12", StickerCategory.BEWAFA, "Midnight Rain", "☾⋆🌧️💔🥀⋆☽", listOf("night", "bewafa")),
        StickerItem("bw13", StickerCategory.BEWAFA, "Silent Pain", "😔💧🫂", listOf("pain", "hug")),
        StickerItem("bw14", StickerCategory.BEWAFA, "Broken Wing", "꧁💔🥀🖤🥀💔꧂", listOf("wing", "sad")),

        // 3. Attitude & Royal Crowns
        StickerItem("ry1", StickerCategory.ROYAL, "Royal Crown", "👑", listOf("king", "queen", "royal")),
        StickerItem("ry2", StickerCategory.ROYAL, "Lion King", "🦁", listOf("lion", "attitude", "boss")),
        StickerItem("ry3", StickerCategory.ROYAL, "Apex Eagle", "🦅", listOf("eagle", "attitude")),
        StickerItem("ry4", StickerCategory.ROYAL, "Dragon Crest", "🐉", listOf("dragon", "power")),
        StickerItem("ry5", StickerCategory.ROYAL, "Trident Aura", "🔱", listOf("trident", "royal")),
        StickerItem("ry6", StickerCategory.ROYAL, "Crossed Swords", "⚔️", listOf("swords", "warrior")),
        StickerItem("ry7", StickerCategory.ROYAL, "Diamond Boss", "💎", listOf("diamond", "vip")),
        StickerItem("ry8", StickerCategory.ROYAL, "Cool Shades", "🕶️", listOf("swag", "attitude")),
        StickerItem("ry9", StickerCategory.ROYAL, "Fleur Crest", "⚜️", listOf("crest", "royal")),
        StickerItem("ry10", StickerCategory.ROYAL, "King Frame", "꧁👑༒☬🦁☬༒👑꧂", listOf("king", "royal")),
        StickerItem("ry11", StickerCategory.ROYAL, "VIP Aura", "👑⚡💎🔥", listOf("vip", "boss", "status")),
        StickerItem("ry12", StickerCategory.ROYAL, "Empire Badge", "♛ 𝕽𝕺𝖄𝕬𝕷 ♛", listOf("empire", "king")),
        StickerItem("ry13", StickerCategory.ROYAL, "Boss Line", "═══ ⚜️👑⚜️ ═══", listOf("divider", "royal")),
        StickerItem("ry14", StickerCategory.ROYAL, "Sigma Crown", "🕶️👑⚡🚬", listOf("sigma", "attitude")),

        // 4. Fire & Gaming Symbols
        StickerItem("gm1", StickerCategory.GAMING, "Blaze Fire", "🔥", listOf("fire", "lit", "gaming")),
        StickerItem("gm2", StickerCategory.GAMING, "High Voltage", "⚡", listOf("bolt", "fast", "gaming")),
        StickerItem("gm3", StickerCategory.GAMING, "Boom Impact", "💥", listOf("boom", "clutch", "edit")),
        StickerItem("gm4", StickerCategory.GAMING, "Pro Controller", "🎮", listOf("gaming", "stream")),
        StickerItem("gm5", StickerCategory.GAMING, "Bullseye Shot", "🎯", listOf("aim", "headshot")),
        StickerItem("gm6", StickerCategory.GAMING, "Champion Cup", "🏆", listOf("win", "booyah", "champion")),
        StickerItem("gm7", StickerCategory.GAMING, "Rocket Launch", "🚀", listOf("boost", "viral")),
        StickerItem("gm8", StickerCategory.GAMING, "Anger Sparks", "💢", listOf("anime", "rage")),
        StickerItem("gm9", StickerCategory.GAMING, "Clan Wings", "꧁༒☬🔥⚡🔥☬༒꧂", listOf("ff", "pubg", "clan")),
        StickerItem("gm10", StickerCategory.GAMING, "Sniper Tag", "▄︻デ══━一💥", listOf("gun", "sniper", "ff")),
        StickerItem("gm11", StickerCategory.GAMING, "Sword Duo", "⚔️🔥⚡🛡️", listOf("battle", "clan")),
        StickerItem("gm12", StickerCategory.GAMING, "ESports Frame", "◤🎮🔥⚡🔥🎮◥", listOf("esports", "youtube")),
        StickerItem("gm13", StickerCategory.GAMING, "Kill Streak", "💀🔥🎯🏆", listOf("clutch", "montage")),
        StickerItem("gm14", StickerCategory.GAMING, "Star Clash", "★彡[ 🔥⚡ ]彡★", listOf("clash", "tag")),

        // 5. Aesthetic Text Art
        StickerItem("ae1", StickerCategory.AESTHETIC, "Cute Hug", "(づ｡◕‿‿◕｡)づ", listOf("kaomoji", "cute")),
        StickerItem("ae2", StickerCategory.AESTHETIC, "Bear Love", "ʕ•́ᴥ•̀ʔっ♡", listOf("bear", "cute")),
        StickerItem("ae3", StickerCategory.AESTHETIC, "Table Flip", "(╯°□°）╯︵ ┻━┻", listOf("flip", "funny")),
        StickerItem("ae4", StickerCategory.AESTHETIC, "Sparkle Eyes", "♡(ŐωŐ人)", listOf("anime", "sparkle")),
        StickerItem("ae5", StickerCategory.AESTHETIC, "Star Divider", "─── ⋆⋅☆⋅⋆ ───", listOf("divider", "caption")),
        StickerItem("ae6", StickerCategory.AESTHETIC, "Moon Divider", "•─────⋅☾ ☽⋅─────•", listOf("moon", "bio")),
        StickerItem("ae7", StickerCategory.AESTHETIC, "Sparkle Trail", "˚₊· ͟͟͞͞➳❥ ✨", listOf("arrow", "aesthetic")),
        StickerItem("ae8", StickerCategory.AESTHETIC, "Constellation", "⋆｡°✩ ✧˖° ✩°｡⋆", listOf("stars", "reel")),
        StickerItem("ae9", StickerCategory.AESTHETIC, "Floral Frame", "✿•┈๑⋅⋯ ⋯⋅๑┈•✿", listOf("flower", "frame")),
        StickerItem("ae10", StickerCategory.AESTHETIC, "Music Vibe", "ılılıll|̲̅̅●̲̅̅|̲̅̅=̲̅̅|̲̅̅●̲̅̅|llılılı", listOf("music", "reel", "audio")),
        StickerItem("ae11", StickerCategory.AESTHETIC, "Butterfly Glow", "🦋✨🌸💫", listOf("butterfly", "pastel")),
        StickerItem("ae12", StickerCategory.AESTHETIC, "Ribbon Bow", "୨୧ ‧₊˚ ⋅ ♡ ⋅ ˚₊‧ ୨୧", listOf("coquette", "ribbon")),
        StickerItem("ae13", StickerCategory.AESTHETIC, "Loading Bar", "███▒▒▒▒▒▒▒ 30%", listOf("loading", "bio")),
        StickerItem("ae14", StickerCategory.AESTHETIC, "Minimal Box", "┌── ⋆⋅☆⋅⋆ ──┐", listOf("box", "header")),
    )

    val bioTemplates: List<BioTemplate> = listOf(
        // Gaming
        BioTemplate("g1", CreatorPlatform.GAMING, "FF / PUBG Clan Wing", "꧁༒☬ 𝕹𝖊𝖔𝖓 ☬༒꧂") { name ->
            "꧁༒☬ ${UnicodeFontEngine.transformEnglish(name, "bold_gothic")} ☬༒꧂"
        },
        BioTemplate("g2", CreatorPlatform.GAMING, "Sniper Headshot Tag", "▄︻デ 𝐍𝐞𝐨𝐧 ━一💥") { name ->
            "▄︻デ ${UnicodeFontEngine.transformEnglish(name, "serif_bold")} ━一💥"
        },
        BioTemplate("g3", CreatorPlatform.GAMING, "ESports Pro Squad", "◤⚡ 𝗡𝗲𝗼𝗻 ⚡◥") { name ->
            "◤⚡ ${UnicodeFontEngine.transformEnglish(name, "sans_bold")} ⚡◥"
        },
        BioTemplate("g4", CreatorPlatform.GAMING, "Dark Skull Gamer", "💀🔥 𝙉𝙚𝙤𝙣 🔥💀") { name ->
            "💀🔥 ${UnicodeFontEngine.transformEnglish(name, "sans_bold_italic")} 🔥💀"
        },
        BioTemplate("g5", CreatorPlatform.GAMING, "Royal Gaming Boss", "👑⚔️ 𝓝𝓮𝓸𝓷 ⚔️👑") { name ->
            "👑⚔️ ${UnicodeFontEngine.transformEnglish(name, "bold_script")} ⚔️👑"
        },
        BioTemplate("g6", CreatorPlatform.GAMING, "Star Clash Tag", "★彡[ 𝙽𝚎𝚘𝚗 ]彡★") { name ->
            "★彡[ ${UnicodeFontEngine.transformEnglish(name, "monospace")} ]彡★"
        },

        // YouTube
        BioTemplate("yt1", CreatorPlatform.YOUTUBE, "Creator Channel Header", "🎬✨ 𝗡𝗲𝗼𝗻 𝗦𝘁𝘂𝗱𝗶𝗼 ✨") { name ->
            "🎬✨ ${UnicodeFontEngine.transformEnglish(name, "sans_bold")} ✨\n📺 Daily Shorts & Edits\n🔔 Subscribe & Turn On Bell"
        },
        BioTemplate("yt2", CreatorPlatform.YOUTUBE, "Cyber Tech Channel", "『 ℕ𝕖𝕠𝕟 』⚡") { name ->
            "『 ${UnicodeFontEngine.transformEnglish(name, "double_struck")} 』⚡\n🎥 4K Edits • Presets • Tutorials"
        },
        BioTemplate("yt3", CreatorPlatform.YOUTUBE, "Bengali Vlogger Bio", "✿ ── Neon ── ✿") { name ->
            "✿ ── $name ── ✿\n🎥 নতুন ভিডিও প্রতিদিন\n❤️ ভালো লাগলে সাবস্ক্রাইব করুন"
        },
        BioTemplate("yt4", CreatorPlatform.YOUTUBE, "Music & Lofi Channel", "🎧 𝒩𝑒𝑜𝓃 • 8D") { name ->
            "🎧 ${UnicodeFontEngine.transformEnglish(name, "script")} ılılıll|̲̅̅●̲̅̅|̲̅̅=̲̅̅|̲̅̅●̲̅̅|llılılı\n☾⋆⁺₊⋆ Late Night Vibes ⋆⁺₊⋆☽"
        },

        // Instagram
        BioTemplate("ig1", CreatorPlatform.INSTAGRAM, "Aesthetic Reel Creator", "˚₊· 𝓝𝓮𝓸𝓷 ➳❥") { name ->
            "˚₊· ͟͟͞͞➳❥ ${UnicodeFontEngine.transformEnglish(name, "bold_script")}\n✧˖° Visual Storyteller\n📍 Creating Magic Daily ✨"
        },
        BioTemplate("ig2", CreatorPlatform.INSTAGRAM, "Minimalist Small Caps", "ɴᴇᴏɴ • ᴄʀᴇᴀᴛᴏʀ") { name ->
            "${UnicodeFontEngine.transformEnglish(name, "small_caps")} • ᴄʀᴇᴀᴛᴏʀ\n─── ⋆⋅☆⋅⋆ ───\n✨ ᴅᴍ ꜰᴏʀ ᴄᴏʟʟᴀʙꜱ"
        },
        BioTemplate("ig3", CreatorPlatform.INSTAGRAM, "Luxury Dark Attitude", "👑 𝕹𝖊𝖔𝖓 🖤") { name ->
            "👑 ${UnicodeFontEngine.transformEnglish(name, "bold_gothic")} 🖤\n⚡ Rule Your Own Story\n💎 No Copies, Only Original"
        },
        BioTemplate("ig4", CreatorPlatform.INSTAGRAM, "Coquette Sparkle", "୨୧ 𝒩𝑒𝑜𝓃 ୨୧") { name ->
            "୨୧ ‧₊˚ ${UnicodeFontEngine.transformEnglish(name, "script")} ˚₊‧ ୨୧\n🦋 Dreaming in Neon\n🌸 Reels & Aesthetics"
        },
        BioTemplate("ig5", CreatorPlatform.INSTAGRAM, "Bewafa / Sad Poetry", "💔🥀 Neon 🥀💔") { name ->
            "💔🥀 $name 🥀💔\n☾⋆ অনুভূতির ছোট্ট ডায়েরি\n🌧️ ভাঙা মনের গল্প"
        },

        // Facebook
        BioTemplate("fb1", CreatorPlatform.FACEBOOK, "VIP Profile Frame", "♛ 𝐍𝐞𝐨𝐧 ♛") { name ->
            "♛ ${UnicodeFontEngine.transformEnglish(name, "serif_bold")} ♛\n🔥 Attitude Depends On You\n👑 King Of Own World"
        },
        BioTemplate("fb2", CreatorPlatform.FACEBOOK, "Bengali Royal Bio", "꧁👑 Neon 👑꧂") { name ->
            "꧁༒☬ $name ☬༒꧂\n🌿 সহজ-সরল জীবন, বড় স্বপ্ন\n⚡ নিজের মতো করে বাঁচি"
        },
        BioTemplate("fb3", CreatorPlatform.FACEBOOK, "Bubble Fun Name", "Ⓝⓔⓞⓝ ✨") { name ->
            "${UnicodeFontEngine.transformEnglish(name, "bubble")} ✨\n💫 Spreading Positive Energy\n📸 Photography & Memes"
        },
        BioTemplate("fb4", CreatorPlatform.FACEBOOK, "Dark Badge Name", "🅽🅴🅾🅽 🔥") { name ->
            "${UnicodeFontEngine.transformEnglish(name, "dark_square")} 🔥\n🎮 Gamer | 🎬 Editor | ⚡ Creator"
        },
        BioTemplate("fb5", CreatorPlatform.FACEBOOK, "Vaporwave Profile", "Ｎｅｏｎ ☾⋆") { name ->
            "${UnicodeFontEngine.transformEnglish(name, "fullwidth")} ☾⋆\n•─────⋅☾ ☽⋅─────•\n⚡ Digital Creator"
        },
    )
}
