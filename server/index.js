const express = require("express");
const Pusher = require("pusher");
const bodyParser = require("body-parser");
const appConfig = require("./config.json");

const app = express();
const port = parseInt(appConfig.port) || 4040;
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

const pusher = new Pusher({
  appId: appConfig.app_id,
  key: appConfig.key,
  secret: appConfig.secret,
  cluster: appConfig.cluster
});

app.post("/location", (req, res) => {
  const longitude = req.body.longitude;
  console.log("long", longitude);
  const latitude = req.body.latitude;
  console.log("lat", latitude);
  const username = req.body.username;
  console.log("user", username);

  pusher.trigger("feed", "location", {longitude, latitude, username});
  res.json({success: 200});
});

app.listen(port, function () {
  console.log(`Listening on ${port}`)
});
