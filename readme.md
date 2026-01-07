# LionAPI 
is my core project and is required by most of my plugins.<br>
It provides a basic challenge and team API that can be used in plugins that require
- a timer (and additional functions like ending the challenge when a player or the dragon dies)
- teams / backpacks
- Addon API to allow simple features of plugins to be configured / en-/disabled in-game
- additional worlds and tools like cloning and resetting worlds
- multi server setups (server communication)
- velocity connections 
- special GUIs
- and more
<br>

# Version History
https://github.com/LionCraft08/Downloads/tree/main/LionAPI

# Usage
- Commands: 
- - /ls: Opens the configuration menu
- - /code: Executes Java Code (Disabled by default due to security risks) 
- - /teams: Manage Teams (Useful for backpack and battles like ForceItemBattle)
- Chat: LionPlugins use LionChat, meaning every channel can be adjusted and disabled

# Additional Information 
The usage of the /code command is a huge security risk and should only be enabled when you actually need it. Enabling this feature requires the server to run in a jdk environment, NOT a jre, as this feature needs the java compiler.

