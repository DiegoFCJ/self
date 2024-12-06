import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

# Constants
BASE_URL = 'https://www.facebook.com'
USER_PROFILE = 'YOUR_PROFILE_NAME_TAKEN_BY_LINK' # Here you should put the name in your profile.. you have to take that from the link where you want to delete photos
LOCALE = '?locale=en_EN' # If you change this value you should change COOKIE_BUTTON_TEXT to otherwise it won't work
PHOTOS_PAGE_URL = f"{BASE_URL}/{USER_PROFILE}/photos_by{LOCALE}"

EMAIL = "YOUR_EMAIL"  # Replace with your email
PASSWORD = "YOUR_PASSWORD!"  # Replace with your password

# Login and cookie management
COOKIE_BUTTON_TEXT = "Decline optional cookies"
EMAIL_FIELD_NAME = "email"
PASSWORD_FIELD_NAME = "pass"

# Classes and XPaths
IMAGES_CLASS_NAME = 'x78zum5.x1q0g3np.x1a02dak'
EDIT_BUTTON_CLASS_NAME = 'x1923su1.x10l6tqk.xfr5jun'
DELETE_PHOTO_BUTTON_XPATH = "//span[contains(text(),'Delete photo')]"
DELETE_CONFIRM_BUTTON_XPATH = "//span[contains(text(),'Delete')]"
SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView();"

# Path to ChromeDriver
CHROMEDRIVER_PATH = '/home/diego/Documents/chromedriver/chromedriver'

# Chrome options to disable notifications
chrome_options = Options()
chrome_options.add_argument("--disable-notifications")
chrome_options.add_argument("--disable-popup-blocking")
chrome_options.add_argument("--disable-features=Notification")

# Use Service to specify the ChromeDriver path
service = Service(executable_path=CHROMEDRIVER_PATH)

# Start the driver with the specified options
driver = webdriver.Chrome(service=service, options=chrome_options)

# Print messages as constants
MSG_COOKIE_BUTTON_CLICKED = f"{COOKIE_BUTTON_TEXT} button clicked"
MSG_COOKIE_BUTTON_CLICKED_FOR_SEARCHING = f"//span[text()='{COOKIE_BUTTON_TEXT}']"
MSG_COOKIE_BUTTON_ERROR = "Error clicking the 'Reject optional cookies' button: {}"
MSG_EMAIL_ENTERED = "Email entered"
MSG_EMAIL_ERROR = "Error entering email: {}"
MSG_PASSWORD_ENTERED = "Password entered"
MSG_PASSWORD_ERROR = "Error entering password: {}"
MSG_LOGIN_SUBMITTED = "Login submitted"
MSG_LOGIN_ERROR = "Error submitting password: {}"

MSG_NO_IMAGES_FOUND = "No images found."
MSG_EDIT_BUTTON_CLICKED = "Edit button clicked"
MSG_DELETE_PHOTO_BUTTON_CLICKED = "'Delete photo' button clicked, popup opened"
MSG_DELETE_CONFIRM_BUTTON_CLICKED = "'Delete' button in popup clicked"
MSG_PHOTO_DELETED = "Photo deleted, moving to the next one"
MSG_EDIT_DELETE_ERROR = "Error during edit or delete process: {}"
MSG_GENERAL_ERROR = "General error: {}"

# Function to perform login
def login():
    driver.get(PHOTOS_PAGE_URL)
    time.sleep(2)

    # Click on the "Reject optional cookies" button
    try:
        reject_cookies_button = WebDriverWait(driver, 3).until(
            EC.presence_of_element_located((By.XPATH, MSG_COOKIE_BUTTON_CLICKED_FOR_SEARCHING))
        )
        reject_cookies_button.click()
        print(MSG_COOKIE_BUTTON_CLICKED)
    except Exception as e:
        print(MSG_COOKIE_BUTTON_ERROR.format(e))

    time.sleep(2)

    # Enter email into the input field
    try:
        email_input = WebDriverWait(driver, 2).until(
            EC.presence_of_element_located((By.NAME, EMAIL_FIELD_NAME))
        )
        email_input.send_keys(EMAIL)
        print(MSG_EMAIL_ENTERED)
    except Exception as e:
        print(MSG_EMAIL_ERROR.format(e))

    # Enter password into the input field
    try:
        password_input = WebDriverWait(driver, 2).until(
            EC.presence_of_element_located((By.NAME, PASSWORD_FIELD_NAME))
        )
        password_input.send_keys(PASSWORD)
        print(MSG_PASSWORD_ENTERED)
    except Exception as e:
        print(MSG_PASSWORD_ERROR.format(e))

    # Press Enter to log in
    try:
        password_input.send_keys(Keys.RETURN)
        print(MSG_LOGIN_SUBMITTED)
    except Exception as e:
        print(MSG_LOGIN_ERROR.format(e))

    time.sleep(3)

# Function to delete photos
def delete_photos():
    driver.get(PHOTOS_PAGE_URL)
    time.sleep(3)

    try:
        while True:
            images = driver.find_elements(By.CLASS_NAME, IMAGES_CLASS_NAME)

            if not images:
                print(MSG_NO_IMAGES_FOUND)
                break

            for image in images:
                try:
                    edit_button = WebDriverWait(driver, 5).until(
                        EC.element_to_be_clickable((By.CLASS_NAME, EDIT_BUTTON_CLASS_NAME))
                    )
                    edit_button.click()
                    print(MSG_EDIT_BUTTON_CLICKED)
                    time.sleep(2)

                    delete_photo_button = WebDriverWait(driver, 10).until(
                        EC.element_to_be_clickable((By.XPATH, DELETE_PHOTO_BUTTON_XPATH))
                    )
                    delete_photo_button.click()
                    print(MSG_DELETE_PHOTO_BUTTON_CLICKED)

                    delete_confirm_button = WebDriverWait(driver, 10).until(
                        EC.element_to_be_clickable((By.XPATH, DELETE_CONFIRM_BUTTON_XPATH))
                    )
                    driver.execute_script(SCROLL_INTO_VIEW_SCRIPT, delete_confirm_button)
                    time.sleep(1)
                    delete_confirm_button.click()
                    print(MSG_DELETE_CONFIRM_BUTTON_CLICKED)

                    time.sleep(2)
                    print(MSG_PHOTO_DELETED)

                    driver.get(PHOTOS_PAGE_URL)
                    time.sleep(3)
                    break
                except Exception as e:
                    print(MSG_EDIT_DELETE_ERROR.format(e))
                    continue
    except Exception as e:
        print(MSG_GENERAL_ERROR.format(e))

login()
delete_photos()
driver.quit()