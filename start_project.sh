#!/bin/bash

# Funzione per verificare il sistema operativo
function get_os() {
    case "$(uname -s)" in
        Darwin)
            echo "macOS"
            ;;
        Linux)
            echo "Linux"
            ;;
        CYGWIN*|MINGW32*|MSYS*|MINGW*)
            echo "Windows"
            ;;
        *)
            echo "Unknown OS"
            ;;
    esac
}

# Funzione per eseguire il comando su macOS/Linux
function run_on_unix() {
    echo "Pulizia del progetto..."
    flutter clean

    echo "Avvio del progetto..."
    flutter run
}

# Funzione per eseguire il comando su Windows (utilizzando Git Bash o terminale compatibile)
function run_on_windows() {
    echo "Pulizia del progetto..."
    flutter clean

    echo "Avvio del progetto..."
    flutter run
}

# Funzione principale
function main() {
    # Rileva il sistema operativo
    OS=$(get_os)
    echo "Sistema operativo rilevato: $OS"

    if [ "$OS" == "macOS" ] || [ "$OS" == "Linux" ]; then
        # Esegui su macOS/Linux
        run_on_unix
    elif [ "$OS" == "Windows" ]; then
        # Esegui su Windows (usando Git Bash o terminale compatibile)
        run_on_windows
    else
        echo "Sistema operativo non supportato"
        exit 1
    fi
}

# Avvia lo script
main