/**
 * Created by suyhuai on 2015/12/15.
 */
var mongoose = require('mongoose');
//mongoose.connect('mongodb://localhost/AI');
var db = mongoose.createConnection('localhost','AI');

/*db.on('error',console.error.bind(console,'连接错误:'));
db.once('open',function(){
    console.log('打开了:')
});*/

var PersonSchema = new mongoose.Schema({
    name:String
    //_id:mongoose.Schema.Types.ObjectId
});

var PersonModel = db.model('annimal',PersonSchema);
var personEntity = new PersonModel({name:'dog'});
console.log(personEntity.name);
personEntity.save();

PersonModel.find(function(err,persons){
   console.log(persons);
});
exports.mongoose = mongoose;