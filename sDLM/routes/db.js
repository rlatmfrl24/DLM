var express = require('express');
var mysql = require('mysql');
var router = express.Router();
var connection = mysql.createConnection({
  host : '35.233.230.219',
  user : 'root',
  password: 'Love397!@',
  prot: 3306,
  database: 'test_trends'
});
connection.connect(function(err){
  if(err) res.send(err);
});

/* GET users listing. */
router.get('/', function(res) {
  res.send('DB RESTful API Page');
});

router.get('/hiyobi/all', function(res){
  connection.query('SELECT * from tb_hiyobi_info', function(err, rows){
    if(!err) res.send(rows);
    else console.error(err);
  });
});

router.get('/link', function(res){
  res.send('Request Format for tb_link_info')
});

router.post('/link', function(req, res){
  var sql = "SELECT * from tb_link_info info where "+req.body.column+" like '"+req.body.value+"';";
  connection.query(sql, function(err, rows){
    if(!err) res.send(rows);
    else res.send(err);
  });
});

router.get('/link/all', function(res){
  connection.query('SELECT * from tb_link_info', function(err, rows, fields){
    if(!err) res.send(rows);
    else console.error(err);
  });
});

module.exports = router;
