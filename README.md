```
  _________                                 __         .__     
 /   _____/____    ______________ _______ _/  |_  ____ |  |__  
 \_____  \\__  \  /  ___/ ____/  |  \__  \\   __\/ ___\|  |  \ 
 /        \/ __ \_\___ < <_|  |  |  // __ \|  | \  \___|   Y  \
/_______  (____  /____  >__   |____/(____  /__|  \___  >___|  /
        \/     \/     \/   |__|          \/          \/     \/ 
 __      __               __  .__                              
/  \    /  \ ____ _____ _/  |_|  |__   ___________             
\   \/\/   // __ \\__  \\   __\  |  \_/ __ \_  __ \            
 \        /\  ___/ / __ \|  | |   Y  \  ___/|  | \/            
  \__/\  /  \___  >____  /__| |___|  /\___  >__|               
       \/       \/     \/          \/     \/                   
```


Sasquatch Weather is a personal project that fetches weather information using the OpenWeatherMap API. The application allows you to check the current weather conditions for a specified city.

## Features

- Current Temperatures in C, F, and K
- A description of the weather in your area (e.g. Partly Cloudy)
- Time-of-day backgrounds
- MORE COMING SOON

## Prerequisites

Before running Sasquatch Weather, make sure you have the following:

- OpenWeatherMap API key (Sign up [here](https://openweathermap.org/) to obtain your API key for free)

## Configuration

1. Create a `.env` file in the root directory of the project.
2. Add your OpenWeatherMap API key and the desired city name in `.env` file:

```bash
export API_KEY=your_openweathermap_api_key
export CITY_NAME=your_city_name
```

3. Type chmod +x run.sh in your terminal to allow your system to run to the script
4. Type ./run.sh in your terminal to set the environment variables and compile the files

Note: This is running on macOS Monterey 12.7.1 and is (at least currently) only set up to work on my own system, I cannot guarantee that these steps will work for everyone, but it should be pretty straightforward; and it is not even intended to work for anyone else's system as I am still developing this. However, if you intend to use this in its current state the most you should have to work with and edit are the environment variables. I'm currently doing the environment variables in a "hacky" way.
