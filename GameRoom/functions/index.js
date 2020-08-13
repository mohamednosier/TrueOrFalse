const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const firebase = require('firebase-admin');

var config = {
apiKey: "AIzaSyAoI7Tjy_5Ee8b97UGE-hNWd6B5XAmKs60",
authDomain: "truefalse-4b498.firebaseapp.com",
databaseURL: "https://truefalse-4b498.firebaseio.com/",
storageBucket: "gs://truefalse-4b498.appspot.com/"
  };
  firebase.initializeApp(config);


exports.create=functions.https.onRequest((req,res)=>{

  var data=JSON.parse(req.body);
  var user_id=data.user_id;

var language_id=data.language_id;
var que_id = data.que_id;
  firebase.database().ref("game_room/").orderByChild("availability").equalTo(1).limitToFirst(1).once('value', ((snapshot)=> {
     console.log("user_id  ",user_id);


    if (snapshot.val() !== null) {
            console.log("available");
            var childKey = Object.keys(snapshot.val());
            console.log("gameroom   ", childKey)


            const userQuery = firebase.database().ref(`game_room/${childKey}`).once('value');
            return userQuery.then(userResult => {
                const language_id1 = userResult.val().language_id;

                if (language_id === language_id1) {
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

                }else{
					createGameRoom();
				}
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
                                        "que_id":que_id,
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
