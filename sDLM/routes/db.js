var express = require('express');
var mysql = require('mysql');
var router = express.Router();
var db_config = {
  host: '35.233.250.217',
  user: 'root',
  password: 'Love397!@',
  prot: 3306,
  database: 'db_trends'
}
var test_config = {
  host: '35.233.250.217',
  user: 'root',
  password: 'Love397!@',
  prot: 3306,
  database: 'test_trends'
}

function handleDisconnect(config) {
  console.log('handleDisconnect()');
  connection.destroy();
  connection = mysql.createConnection(config);
  connection.connect(function (err) {
    if (err) {
      console.log(' Error when connecting to db  (DBERR001):', err);
      setTimeout(handleDisconnect, 1000);
    }
  });
}

/* GET users listing. */
router.get('/', function (req, res) {
  res.send('DB RESTful API Page');
});

router.post('/', function (req, res) {
  var connection = mysql.createConnection(db_config);
  connection.connect(function (err) {
    if (err) {
      console.log('Connection is asleep (time to wake it up): ', err);
      setTimeout(handleDisconnect, 1000);
      handleDisconnect(db_config);
    }
  });
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
      else {
        console.log(err);
        res.send(err);
      }
    });
  } else if (req.body.query_type == 'INSERT') {
    sql += "INSERT IGNORE INTO " + req.body.table_name + " " + req.body.column + " VALUES ?";
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

router.post('/test', function (req, res) {
  var connection = mysql.createConnection(test_config);
  connection.connect(function (err) {
    if (err) {
      console.log('Connection is asleep (time to wake it up): ', err);
      setTimeout(handleDisconnect, 1000);
      handleDisconnect(test);
    }
  });
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
      else {
        console.log(err);
        res.send(err);
      }
    });
  } else if (req.body.query_type == 'INSERT') {
    sql += "INSERT IGNORE INTO " + req.body.table_name + " " + req.body.column + " VALUES ?";
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
