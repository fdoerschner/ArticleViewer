# Article Viewer App

## Architecture

The architecture is built upon the modularization pattern to keep the app scalable. https://developer.android.com/topic/modularization
Although the app in it's current state is too small to justify this scale of architecture, but possible features like a barcode scanner 
let me to believe this would still be the right approach.
Ui and viewmodel are built in a model-driven pattern with viewstates as their middle man. Communication between ui und viewmodel happens
only over it's viewstate.
Features are not directly communicating with one another. If they need to access or call functions of another feature it is either 
handled by a navigator which should be implemented by the app itself or by lower level classes like a common data library.

## Database and -fetcher

The initial data used in this app is fetched from a tabulator separated csv file which is loaded and typed with kotlins DataFrame if the
database has no articles loaded. The fetchers implementation could be exchanged to any api fetching service but is only in this state a 
local file loader.
The database is Android Room database holding a table of articles. 
The app has to provide the data for the database.