// script.js

document.addEventListener('DOMContentLoaded', function() {
    const moodInput = document.getElementById('mood');
    const suggestButton = document.getElementById('suggest-button');
    const resultDiv = document.getElementById('result');
    const playlistContainer = document.getElementById('playlist-container');

    // Mood playlist database
    const playlists = {
        happy: [
            {
                name: "Happy Hits!",
                description: "Boost your mood with these feel-good tracks!",
                image: "images/HappyHits.png",
                songCount: "100 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DXdPec7aLTmlC"
            },
            {
                name: "Good Vibes",
                description: "Uplifting and cheerful music for a positive day.",
                image: "images/GoodVibes.png",
                songCount: "134 songs",
                link: "https://open.spotify.com/playlist/56n3ojPxRr5mhQ3NrZJQxa"
            },
            {
                name: "Sunny Day Tunes",
                description: "Bright and joyful melodies to brighten your day.",
                image: "images/SunnyDayTunes.png",
                songCount: "343 songs",
                link: "https://open.spotify.com/playlist/5TbpmhQMr7lMqkNlcCcRZq"
            }
        ],
        sad: [
            {
                name: "Sad Songs",
                description: "For moments when you need a comforting soundtrack.",
                image: "https://i.scdn.co/image/ab67706f000000035f53d20ea4be4b36d2c1fd57",
                songCount: "75 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp3"
            },
            {
                name: "Heartbreak Ballads",
                description: "Emotional songs for getting through tough times.",
                image: "https://i.scdn.co/image/ab67706f0000000314b9341283a80327a92990ba",
                songCount: "85 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp4"
            },
            {
                name: "Gloomy Tunes",
                description: "Reflective and melancholic melodies for introspection.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "60 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp5"
            }
        ],
        energetic: [
            {
                name: "Workout Beats",
                description: "Power up your workout with these high-energy tracks!",
                image: "https://i.scdn.co/image/ab67706f00000003c5fa2f6ed58561b5af3c33f9",
                songCount: "90 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp6"
            },
            {
                name: "Energy Boost",
                description: "Get your adrenaline pumping with these intense tracks.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "110 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp7"
            },
            {
                name: "Dancefloor Fillers",
                description: "Get the party started with these dance anthems!",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "105 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp8"
            }
        ],
        relaxed: [
            {
                name: "Chill Vibes",
                description: "Unwind and relax with these peaceful and calm melodies.",
                image: "https://i.scdn.co/image/ab67706f00000003e4eadd417a05b2546e866934",
                songCount: "80 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX2sUQwD7tWnp9"
            },
            {
                name: "Acoustic Chill",
                description: "Gentle acoustic tracks for a laid-back atmosphere.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "95 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT0"
            },
            {
                name: "Ambient Sounds",
                description: "Immerse yourself in calming ambient textures.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "120 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT1"
            }
        ],
        focused: [
            {
                name: "Deep Focus",
                description: "Enhance your concentration with these instrumental pieces.",
                image: "https://i.scdn.co/image/ab67706f000000036007618fa8be8395b8e0e0b7",
                songCount: "150 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT2"
            },
            {
                name: "Study Beats",
                description: "Lo-fi and chillhop beats perfect for studying and working.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "130 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT3"
            },
            {
                name: "Concentration Music",
                description: "Minimize distractions and maximize focus with this playlist.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "115 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT4"
            }
        ],
        romantic: [ // Added 'romantic' mood
            {
                name: "Romantic Getaway",
                description: "Set the mood with these love-inspired songs.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "90 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT5"
            },
            {
                name: "Love Songs",
                description: "Classic and contemporary love songs for every romantic moment.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "105 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT6"
            }
        ],
        motivated: [ // Added 'motivated' mood
            {
                name: "Motivation Mix",
                description: "Get inspired and driven with these motivational tracks.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "110 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT7"
            },
            {
                name: "Power Songs",
                description: "Unleash your inner power with these empowering anthems.",
                image: "https://i.scdn.co/image/ab67706f00000003a8eff52943c4539f095ff5ff", // Placeholder image
                songCount: "95 songs",
                link: "https://open.spotify.com/playlist/37i9dQZF1DX8Fwnv2jQkLT8"
            }
        ]
    };

    // Mood keywords mapping - expanded for wider input
    const moodKeywords = {
        happy: ['happy', 'joy', 'excited', 'cheerful', 'good', 'great', 'wonderful', 'delighted', 'upbeat', 'positive', 'blessed', 'grateful', 'sunny', 'bright', 'joyful', 'elated', 'content', 'pleased', 'thrilled'],
        sad: ['sad', 'down', 'depressed', 'blue', 'gloomy', 'unhappy', 'miserable', 'sorrowful', 'heartbroken', ' Tearful', 'melancholy', 'pessimistic', 'despondent', 'disheartened'],
        energetic: ['energetic', 'pumped', 'active', 'hyper', 'energized', 'motivated', 'dynamic', 'vigorous', 'lively', 'spirited', 'animated', 'powerful', 'intense', 'fast-paced'],
        relaxed: ['relaxed', 'calm', 'peaceful', 'mellow', 'chill', 'tranquil', 'serene', 'soothing', 'gentle', 'quiet', 'placid', 'still', 'restful', 'laid-back'],
        focused: ['focused', 'concentrated', 'productive', 'studying', 'work', 'attentive', 'absorbed', 'engrossed', 'intent', 'mindful', 'diligent', 'earnest', 'purposeful'],
        romantic: ['romantic', 'love', 'affectionate', 'passionate', 'loving', 'devoted', 'amorous', 'tender', 'intimate', 'sentimental', 'lovey-dovey', 'courtly'], // Added romantic keywords
        motivated: ['motivated', 'inspired', 'driven', 'ambitious', 'determined', 'enthusiastic', 'zealous', 'eager', 'inspired', 'go-getting', 'purposeful', 'resolute', 'strong-willed'] // Added motivated keywords
    };

    suggestButton.addEventListener('click', function() {
        findPlaylist();
    });

    function findPlaylist() {
        const moodText = moodInput.value.toLowerCase().trim();

        if (!moodText) {
            alert('Please enter your mood!');
            return;
        }

        // Find matching mood category
        let matchedMood = null;
        for (const [mood, keywords] of Object.entries(moodKeywords)) {
            if (keywords.some(keyword => moodText.includes(keyword))) {
                matchedMood = mood;
                break;
            }
        }

        // Default to 'happy' if no match found
        const moodCategory = matchedMood || 'happy';
        const playlistOptions = playlists[moodCategory];
        const playlist = playlistOptions[Math.floor(Math.random() * playlistOptions.length)]; // Random playlist

        // Update UI
        playlistContainer.innerHTML = `
            <div class="playlist-image" style="background-image: url(${playlist.image});"></div>
            <h3 class="playlist-name">${playlist.name}</h3>
            <p class="playlist-description">${playlist.description}</p>
            <p class="song-count">${playlist.songCount}</p>
            <button id="spotify-link-button" class="auth-button">Listen on Spotify</button>
        `;

        // Set the Spotify link after creating the button dynamically
        document.getElementById('spotify-link-button').onclick = () => window.open(playlist.link, '_blank');

        // Show result
        resultDiv.classList.remove('hidden');
    }

    // Allow Enter key to trigger search
    moodInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            findPlaylist();
        }
    });
});