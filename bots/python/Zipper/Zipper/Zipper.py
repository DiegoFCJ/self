import os
import zipfile
import tkinter as tk
from tkinter import filedialog
from tkinter import messagebox

def zip_folder(source_folder, zip_file_path):
    """
    Compress a folder into a ZIP file.
    
    Args:
    - source_folder (str): Path to the folder to compress.
    - zip_file_path (str): Path where the ZIP file should be saved.
    """
    with zipfile.ZipFile(zip_file_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
        # Walk through all files and directories in the source folder
        for root, dirs, files in os.walk(source_folder):
            for file in files:
                file_path = os.path.join(root, file)
                zipf.write(file_path, os.path.relpath(file_path, source_folder))

def browse_folder():
    """
    Open a file dialog to select a folder to zip and the destination path for the ZIP file.
    """
    # Open file dialog to select folder to compress
    source_folder = filedialog.askdirectory(title="Select Folder to Zip")
    if not source_folder:
        return
    
    # Open file dialog to select where to save the ZIP file
    zip_file_path = filedialog.asksaveasfilename(defaultextension=".zip",
                                                 filetypes=[("ZIP files", "*.zip")],
                                                 title="Save ZIP As")
    if not zip_file_path:
        return
    
    # Compress the folder into a ZIP file
    try:
        zip_folder(source_folder, zip_file_path)
        messagebox.showinfo("Success", f"Folder successfully compressed into:\n{zip_file_path}")
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred while zipping the folder: {str(e)}")

# Set up the GUI window using tkinter
def create_gui():
    window = tk.Tk()
    window.title("Zipper Bot")

    label = tk.Label(window, text="Click the button to select a folder to zip.")
    label.pack(pady=10)

    zip_button = tk.Button(window, text="Choose Folder and Zip", command=browse_folder)
    zip_button.pack(pady=20)

    window.mainloop()

# Run the GUI
if __name__ == "__main__":
    create_gui()