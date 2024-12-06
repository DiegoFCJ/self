#!/bin/bash

# Constants
ENV_NAME="facebook_bot_env"
REQUIREMENTS_FILE="requirements.txt"
BOT_SCRIPT="images-remover.py"

# Requirements content
REQUIREMENTS_CONTENT=$(cat <<EOF
selenium
EOF
)

# Check Python availability
if ! command -v python3 &>/dev/null; then
  echo "Python3 is not installed. Please install Python3 and try again."
  exit 1
fi

# Check pip availability
if ! command -v pip3 &>/dev/null; then
  echo "pip3 is not installed. Installing pip3..."
  sudo apt-get install -y python3-pip || { echo "Failed to install pip3"; exit 1; }
fi

# Check if virtualenv is installed
if ! pip3 show virtualenv &>/dev/null; then
  echo "Installing virtualenv..."
  pip3 install virtualenv || { echo "Failed to install virtualenv"; exit 1; }
fi

# Create a requirements file
if [ ! -f "$REQUIREMENTS_FILE" ]; then
  echo "Creating requirements.txt..."
  echo "$REQUIREMENTS_CONTENT" > "$REQUIREMENTS_FILE"
fi

# Create and activate virtual environment
echo "Creating virtual environment: $ENV_NAME"
virtualenv "$ENV_NAME"

# Activate the virtual environment
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "win32" ]]; then
  # Windows
  source "$ENV_NAME/Scripts/activate"
else
  # Linux/Unix
  source "$ENV_NAME/bin/activate"
fi

# Install dependencies
echo "Installing dependencies..."
pip install -r "$REQUIREMENTS_FILE" || { echo "Failed to install dependencies"; deactivate; exit 1; }

# Run the bot script
if [ -f "$BOT_SCRIPT" ]; then
  echo "Running bot script: $BOT_SCRIPT"
  python3 "$BOT_SCRIPT"
else
  echo "Bot script '$BOT_SCRIPT' not found. Please ensure it exists in the current directory."
fi

# Deactivate virtual environment
echo "Deactivating virtual environment..."
deactivate

# Done
echo "Done."
