# TSW Real Weather

This application brings real weather into Train Sim World. Based on the train location, the weather data is loaded from [Open-Meteo](https://open-meteo.com/) and updated every few minutes.

## Setup

1. Add the `-HTTPAPI` parameter as start parameter.
   - **Steam**: Find Train Sim World in your Steam Library, right click on it and select `Properties`. From the dialog that opens, select the `General` tab (which should be the default) and observe the `Launch options` section. Add the parameter there.
2. Start the game and let it create an API key.

## Usage

Once the setup is done, all you need to do is to start this application together with the game. You're game weather should be updated automatically as soon as you start a trip.

## To do

- Update wind
- Update snow
- Fine-tune fog density
- Add compatibility with development builds
- Transition between weather updates
