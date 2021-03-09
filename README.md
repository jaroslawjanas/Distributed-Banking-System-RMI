# Distributed Banking System RMI

## Prerequisites
- UNIX system
- Java 8 or newer

## Setup
1. Pull the repository
2. Go into the `Distributed-Banking-System-RMI/scripts` folder
3. Run `./compile.sh`
4. Start the registry `./start-registry.sh`
5. Start the server `./start-server.sh`
6. Start the client `./start-client.sh`


## Commands
```
 > create [username] [password]  - Create a new user account
 > login [username] [password]   - Login to existing user account
 > balance                       - Returns current bank balance
 > deposit [amount]              - Deposit specified amount into account
 > withdraw [amount]             - Withdraw specified amount into account
 > statement [from] [to]         - Creates a statement between specified dates. Use inputs "* now" to return all transactions or use the following date format: dd/MM/yyyy
 > help                          - Displays this message
 > ping                          - Test connection with the bank server
 > logout                        - Logout current user
 > exit                          - Exits the system
 > clear                         - Clears the console
 > hello                         - Displays the welcome message
```

## [Example](https://i.imgur.com/FEJkIlQ.mp4)
![GIF](Bank_RMI_showcase.gif)
