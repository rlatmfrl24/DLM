var express = require('express');
var reqpromise = require('request-promise')
var mysql = require('mysql')
var cheerio = require('cheerio')
var Promises = require('bluebird')
var puppeteer = require('puppeteer')
var router = express.Router();
var num_search_page = 5
var bp_board_humor = 'http://v12.battlepage.com/??=Board.Humor.Table';
var bp_board_etc = 'http://v12.battlepage.com/??=Board.ETC.Table';
var dd_board = 'http://www.dogdrip.net/index.php?mid=dogdrip&page=';
var db_config = {
    host: '35.233.250.217',
    user: 'root',
    password: 'Love397!@',
    prot: 3306,
    database: 'db_trends'
}

router.get('/', function (req, res) {
    res.send('Trends Check API Page')
});

router.get('/bp', function (req, res) {
    var url_list = []
    var bp_list = []

    for (var i = 1; i <= num_search_page; i++) {
        url_list.push(bp_board_humor + "&page=" + i)
        url_list.push(bp_board_etc + "&page=" + i)
    }

    Promises.each(url_list, function (url) {
        return reqpromise(url)
            .then(function (body) {
                console.log("GET:bp/[Promise] Get Data from request..")
                var $ = cheerio.load(body);
                var $table_data = $('#div_content_containter > div:nth-child(2) > div.detail_container > div.ListTable');
                return $table_data.find('a');
            }).then(function (data) {
                data.each(function () {
                    bp_list.push(this.attribs.href.replace(/&page=[0-9]/gi, ""))
                })
            })
    }).then(function () {
        return new Promise(function (resolve, reject) {
            console.log("GET:bp/[Promise] Connect DB..")
            var connection = mysql.createConnection(db_config);
            connection.connect(function (err) {
                if (err) {
                    console.log('Connection is asleep (time to wake it up): ', err);
                    setTimeout(handleDisconnect, 1000);
                    handleDisconnect();
                }
            });
            var sql = "SELECT link FROM tb_link_info WHERE domain like 'v12.battlepage.com';"
            connection.query(sql, function (err, rows) {
                for (var i = 0; i < rows.length; i++) {
                    var find_data = rows[i].link
                    if (bp_list.includes(find_data)) {
                        bp_list.splice(bp_list.indexOf(find_data), 1)
                    }
                }
                resolve(bp_list)
            })
        }).then(function (row_data) {
            console.log("GET:bp/[Promise] Send to Client..")
            //console.log(row_data)
            res.send(JSON.stringify(row_data))
        })
    })
})
router.get('/dd', function (req, res) {
    var url_list = []
    var dd_list = []

    for (var i = 1; i <= num_search_page; i++) {
        url_list.push(dd_board + i)
    }

    Promises.each(url_list, function (url) {
        return reqpromise(url)
            .then(function (body) {
                console.log("GET:dd/[Promise] Get Data from request")
                var $ = cheerio.load(body);
                var $table_data = $('#main > div > div.eq.section.secontent.background-color-content > div > div.ed.board-list > table > tbody');
                return $table_data.find('.ed.link-reset');
            }).then(function (data) {
                data.each(function () {
                    if (this.attribs.href != '#popup_menu_area') {
                        dd_list.push(this.attribs.href)
                    }
                })
            })
    }).then(function () {
        return new Promise(function (resolve, reject) {
            console.log("GET:dd/[Promise] Connect with DB..")
            var connection = mysql.createConnection(db_config);
            connection.connect(function (err) {
                if (err) {
                    console.log('Connection is asleep (time to wake it up): ', err);
                    setTimeout(handleDisconnect, 1000);
                    handleDisconnect();
                }
            });
            var sql = "SELECT link FROM tb_link_info WHERE domain like 'www.dogdrip.net';"
            connection.query(sql, function (err, rows) {
                for (var i = 0; i < rows.length; i++) {
                    var find_data = rows[i].link
                    if (dd_list.includes(find_data)) {
                        dd_list.splice(dd_list.indexOf(find_data), 1)
                    }
                }
                resolve(dd_list)
            })
        }).then(function (row_data) {
            console.log("GET:dd/[Promise] Send to client..")
            //console.log(row_data)
            res.send(JSON.stringify(row_data))
        })
    })
})

router.get('/hrm', function (req, res) {
    puppeteer.launch()
    .then((browser) => {
        return browser.newPage()
        .then((page) => {
            return page.goto('http://insagirl-toto.appspot.com/hrm/?where=2', {waitUntil:'networkidle2'})
            .then(() => page.$('#hrmbodyexpand'))
            .then((expand) => expand.click())
            .then(() => page.waitFor(1000))
            .then(() => page.$eval('#hrmbody', (element)=>{
                var links = Array.from(element.querySelectorAll('a'))
                return links.map(link=>link.href)
            }))
            .then((data) => {
                return new Promise(function (resolve, reject) {
                    console.log("GET:dd/[Promise] Connect with DB..")
                    data = data.filter(function(item, pos){
                        return data.indexOf(item) == pos;
                    })
                    console.log(data.length)
                    var connection = mysql.createConnection(db_config);
                    connection.connect(function (err) {
                        if (err) {
                            console.log('Connection is asleep (time to wake it up): ', err);
                            setTimeout(handleDisconnect, 1000);
                            handleDisconnect();
                        }
                    });
                    var sql = "SELECT link FROM tb_link_info;"
                    connection.query(sql, function (err, rows) {
                        for (var i = 0; i < rows.length; i++) {
                            var find_data = rows[i].link
                            if (data.includes(find_data)) {
                                data.splice(data.indexOf(find_data), 1)
                            }
                        }
                        resolve(data)
                    })
                }).then(function (row_data) {
                    console.log("GET:dd/[Promise] Send to client..")
                    //console.log(row_data)
                    console.log(row_data.length)
                    res.send(JSON.stringify(row_data))
                })
            })
        })
        .then(() => browser.close())
    }).then(()=>console.log('done!'))
})

function handleDisconnect() {
    console.log('handleDisconnect()');
    connection.destroy();
    connection = mysql.createConnection(db_config);
    connection.connect(function (err) {
        if (err) {
            console.log(' Error when connecting to db  (DBERR001):', err);
            setTimeout(handleDisconnect, 1000);
        }
    });
}

module.exports = router;