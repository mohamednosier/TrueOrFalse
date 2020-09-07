const functions = require('firebase-functions');
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
var private_key = data.private_key;
var timestamp = data.timestamp;


var query = firebase.database().ref("game_room").orderByKey();
query.once("value")
  .then(function(snapshot) {

    var numOfChildren = snapshot.numChildren();
    var counter = 1;
    console.log('numOfChildren : ',numOfChildren);

      if (snapshot.val() !== null) {
        console.log('date before :',new Date());
    snapshot.forEach(function(childSnapshot) {
  if((Date.now() - childSnapshot.val().timestamp) < 40*1000){
      // if(childSnapshot.val().user_id_1 !== user_id){
      if(childSnapshot.val().availability === 1){
if(childSnapshot.val().user_id_1 !== user_id){
        // key will be "ada" the first time and "alan" the second time
        var key = childSnapshot.key;
        console.log('key',key);
        // childData will be the actual contents of the child
        var timestamp = childSnapshot.val().timestamp;
        console.log('timestamp',timestamp);
        var availability = childSnapshot.val().availability;
        console.log('availability',availability);
        console.log('');

        firebase.database().ref(`game_room/${key}/${user_id}`).update({
                 "status":true,
               "right":0,
               "wrong":0,
               "que_no":0,
               "req_continue":0,
               "sel_ans":"",
               "message":"",
                   });

        firebase.database().ref(`game_room/${key}`).update({
                       "availability": 2,
                       "user_id_2":user_id,
                   });
// console.log('date after :',Date.now());
                   console.log('yesssssssssssssssssssss');

      return true;
    }else {
      if(counter === numOfChildren){
      console.log('counter === numOfChildren');
     createGameRoom();
     return true;
      }
    counter++;
    console.log('counter: ',counter);
    }

}else {
  if(counter === numOfChildren){
  console.log('counter === numOfChildren');
 createGameRoom();
 return true;
  }
counter++;
console.log('counter: ',counter);
}

}else {
  if(counter === numOfChildren){
  console.log('counter === numOfChildren');
 createGameRoom();
 return true;
  }
counter++;
console.log('counter: ',counter);
}


// return false;
// }
  });
console.log('date after :',new Date());

}else{
            console.log('Not found any gameroom');
           createGameRoom();
            }
});

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


//
exports.deleteGameRoomId = functions.pubsub.schedule('every 1 minutes').onRun((context) => {
    var timeNow = Date.now();
    console.log('timeNow: ',timeNow);

    var query = firebase.database().ref("game_room").orderByKey();
    query.once("value")
      .then(function(snapshot) {
    snapshot.forEach(function(childSnapshot) {
       if((Date.now() - childSnapshot.val().timestamp) > 3*60*1000){

var key = childSnapshot.key;
                             var adaRef = firebase.database().ref(`game_room/${key}`);
 adaRef.remove()
 .then(function() {
   console.log("Remove succeeded.")
 })
 .catch(function(error) {
   console.log("Remove failed: " + error.message)
 });
console.log('delete game_room: ',key);
       }

      });
});
    return null;
});
