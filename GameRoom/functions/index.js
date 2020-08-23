const functions = require('firebase-functions');
const firebase = require('firebase-admin');

var config = {
apiKey: "AIzaSyAoI7Tjy_5Ee8b97UGE-hNWd6B5XAmKs60",
authDomain: "truefalse-4b498.firebaseapp.com",
databaseURL: "https://truefalse-4b498.firebaseio.com/",
storageBucket: "gs://truefalse-4b498.appspot.com/"
  };
  firebase.initializeApp(config);


  // exports.newNodeDetected = functions.database.ref('game_room/{gameId}/timestamp')
  //     .onWrite((change, context) => {
  //         var timestamp = change.val();
  //         return null;
  //     });

  exports.deleteGameRoom = functions.pubsub.schedule('every 1 minutes').onRun((context) => {
      var timeNow = Date.now();;
      console.log('timeNow: ',timeNow);

      firebase.database().ref(`game_room/`).orderByChild('availability').equalTo(1).limitToFirst(1).once('value', ((snapshot)=> {
         // console.log("user_id pushDataEveryMinute :  ",user_id);


        if (snapshot.val() !== null) {
                // console.log("available");
                var childKey = Object.keys(snapshot.val());
                // console.log("gameroom   ", childKey);


                const userQuery = firebase.database().ref(`game_room/${childKey}`).once('value');

                return userQuery.then(userResult => {
                  const key = Object.keys(userResult.val()) ;
                  console.log("key :  ", key);

                    const timestamp = userResult.val().timestamp;
                    console.log("timestampGameRoom :  ", timestamp);

                    if(timeNow - timestamp > 5*60*1000){
                      console.log("timeout : more than 5 minute  ");
                    }else {
                      console.log("timeout : less than 5 minute  ");
                    }

                });
                console.log(snapshot.val());
            }

        }));

      return null;
  });


   ///////
  // exports.deleteOldItems = functions.database.ref('/path/to/game_room/{pushId}')
// .onWrite((change, context) => {
  // var ref = change.after.ref.parent; // reference to the items
  // var now = Date.now();
  // console.console.log('now: ',now);
  // var cutoff = now -  2 * 60 * 60 * 1000;
  // console.console.log('cutoff: ',cutoff);
  // var oldItemsQuery = ref.orderByChild('timestamp').endAt(cutoff);
  // return oldItemsQuery.once('value', function(snapshot) {
    // create a map with all children that need to be removed
    // console.console.log('cutoff: ',cutoff);
    // var updates = {};
    // snapshot.forEach(function(child) {
      // updates[child.key] = null
    // });
    // execute all updates in one go and return the result to end the function
    // return ref.update(updates);
  // });
// });

// exports.scheduledFunctionPlainEnglish = functions.pubsub.schedule('* * * * *').onRun((context) => {
    // const timeNow = Date.now();
   // console.log('timeNow :',timeNow);

   // const messagesRef = firebase.database().ref('/game_room');
   // messagesRef.once('value', (snapshot) => {
     // console.console.log('hhhhhhhhhh');
    // snapshot.forEach((child) => {

        // if (Number(child.val()['timestamp']) - timeNow  > 20*1000) {
            // return console.log('timestamp :',child.val()['timestamp']);
        // }else {
          // return console.log('timestamp :',child.val()['timestamp']);
        // }
        // });
    // });
// });

// exports.scheduledFunctionPlainEnglish = functions.database.ref("/game_room/{gameId}").onWrite((change, context) => {
//   if (change.after.exists() && !change.before.exists()) {
//     const ref = change.after.ref.parent;
//     const now = Date.now();
//     const oldItemsQuery = ref.orderByChild('timestamp').endAt(now);
//     return oldItemsQuery.once('value').then((snapshot) => {
//       const updates = {};
//       snapshot.forEach(child => {
//         updates[child.key] = null;
//       });
//       return ref.update(updates);
//     });
//   } else {
//     return null;
//   }
// });


exports.create=functions.https.onRequest((req,res)=>{

var data=JSON.parse(req.body);
var user_id=data.user_id;
var language_id=data.language_id;
var que_id = data.que_id;
var private_key = data.private_key;
var timestamp = data.timestamp;

  firebase.database().ref("game_room/").orderByChild("availability").equalTo(1).limitToFirst(1).once('value', ((snapshot)=> {
     console.log("user_id  ",user_id);


    if (snapshot.val() !== null) {
            console.log("available");
            var childKey = Object.keys(snapshot.val());
            console.log("gameroom   ", childKey);


            const userQuery = firebase.database().ref(`game_room/${childKey}`).once('value');

            return userQuery.then(userResult => {
                const language_id1 = userResult.val().language_id;
                const private_key1 = userResult.val().private_key;

                // if (language_id === language_id1) {
				 firebase.database().ref(`game_room/${childKey}/${user_id}`).update({
                        "status": true,
                        "right": 0,
                        "wrong": 0,
                        "que_no": 0,
                        "req_continue":0,
                        "sel_ans": "",
                        "message":"",
                    });
                  firebase.database().ref(`game_room/${childKey}`).update({
                        "availability": 2,
                        "user_id_2":user_id,
                    });

        //         }else{
				// 	createGameRoom();
				// }
            });
            console.log(snapshot.val());
        }
    else
    {

		createGameRoom();


    }

    }));


  function createGameRoom() {
    console.log("not available");
				var ref=firebase.database().ref('game_room/');
			ref.push({
					"availability":1,
          "private_key":private_key,
          "que_id":que_id,
           "timestamp":timestamp,
					"language_id":language_id,
          "user_id_1":user_id,
          "user_id_2":"",

					}).then(function(ref){
						console.log(ref.key);
						var key=ref.key;
							console.log(key);
						 var ref= firebase.database().ref(`game_room/${key}/${user_id}`).update({//.push({
									//ref.set({
							"status":true,
							"right":0,
							"wrong":0,
							"que_no":0,
              "req_continue":0,
							"sel_ans":"",
              "message":"",
								});


						});
}
 res.send({"code":user_id});
});
