# CitAQI
Single activity app to display live air quality monitoring data.
Application is developed with Model–view–ViewModel architectural patterns.

The app will be connected to Web-Socket with Kotlin Channels that will be inserted on the Database based on Room arch. With data inserted view models are used to observe data insertion and fetching to UI Components.

MPAndroidChart Lib is used to display data of City air quality with bar graph with provided color combination and Index value.

The application uses coroutines for better handling of data insertion and fetching from database and Web Socket.

Application has Two-mode.
1. Auto Refresh after 30 sec:
          The application will show latest data after every 30 sec for available cites when connected.

2. Manual navigation: 
          The application will fetch data from the database available from the current showing value with Next/Previous Buttons.
