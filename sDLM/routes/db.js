var express = require('express');
var mysql = require('mysql');
var router = express.Router();
var connection = mysql.createConnection({
  host: '35.233.230.219',
  user: 'root',
  password: 'Love397!@',
  prot: 3306,
  database: 'db_trends'
});

connection.connect(function (err) {
  if (err) {
    console.log(err);
    res.send(err);
  }
});

/* GET users listing. */
router.get('/', function (req, res) {
  res.send('DB RESTful API Page');
});

router.post('/', function (req, res) {


  var sql = "";
  console.log(req.body);
  if (req.body.query_type == 'SELECT') {
    sql += "SELECT " + req.body.column + " FROM " + req.body.table_name;
    if (!req.body.where_value == '') {
      sql += " WHERE " + req.body.where_value + ";"
    }
    console.log(sql);
    connection.query(sql, function (err, rows) {
      if (!err) res.send(rows);
      else res.send(err);
    });
  } else if (req.body.query_type == 'INSERT') {
    sql += "INSERT INTO " + req.body.table_name + " " + req.body.column + " VALUES ?";
    var values = req.body.values;
    connection.query(sql, [values], function (err, result) {
      if (!err) {
        console.log(result);
        res.send(result);
      } else {
        console.log(err);
        res.send(err);
      };
    });
  } else if (req.body.query_type == 'DELETE') {
    sql += "DELETE FROM " + req.body.table_name;
    connection.query(sql, function (err, result) {
      if (!err) res.send(result);
      else res.send(err);
    });
  } else {
    console.log("QUERY ERROR!");
    res.send("Query Type Error");
  }
});

module.exports = router;
