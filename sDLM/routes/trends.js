var express = require('express');
var reqpromise = require('request-promise')
var mysql = require('promise-mysql')
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
    var bp_list = new Array();

    console.log("GET:bp/Request for BP Data")

    for (var i = 1; i <= num_search_page; i++) {
        url_list.push(bp_board_humor + "&page=" + i)
        url_list.push(bp_board_etc + "&page=" + i)
    }

    mysql.createConnection(db_config)
    .then(conn=>{
        var sql = "SELECT link FROM tb_link_info WHERE domain like 'v12.battlepage.com';"
        rows = conn.query(sql)
        return rows
    }).then((rows)=>{
        var except_list = []
        for(var i = 0; i < rows.length; i++){
            except_list.push(rows[i].link)
        }
        Promises.each(url_list, (url)=>{
            return reqpromise(url)
            .then((body)=>{
                console.log("GET:bp/[Promise] Get Data from "+url)
                var $ = cheerio.load(body)
                var $table_data = $('#div_content_containter > div:nth-child(2) > div.detail_container > div.ListTable')
                $table_data.find('td.bp_subject').each(function(){
                    var bp_subject = new Object();
                    bp_subject.title = this.attribs.title
                    bp_subject.link = $(this).find('a').attr('href').replace(/&page=[0-9]/gi, "")
                    if(!except_list.includes(bp_subject.link)){
                        bp_list.push(bp_subject)
                    }
                })
            })
        }).then(()=>{
            console.log(bp_list)
            res.send(JSON.stringify(bp_list))
        })
    })
})
router.get('/dd', function (req, res) {
    var url_list = []
    var dd_list = new Array();

    console.log("GET:dd/Request for DD Data")

    for (var i = 1; i <= num_search_page; i++) {
        url_list.push(dd_board + i)
    }

    mysql.createConnection(db_config)
    .then(conn=>{
        var sql = "SELECT link FROM tb_link_info WHERE domain like 'www.dogdrip.net';"
        rows = conn.query(sql)
        return rows
    }).then((rows)=>{
        var except_list = []
        for(var i = 0; i < rows.length; i++){
            except_list.push(rows[i].link)
        }
        Promises.each(url_list, (url)=>{
            return reqpromise(url)
            .then((body)=>{
                console.log("GET:dd/[Promise] Get Data from "+url)
                var $ = cheerio.load(body)
                var $table_data = $('#main > div > div.eq.section.secontent.background-color-content > div > div.ed.board-list > table > tbody')
                $table_data.find('.ed.link-reset').each(function(){
                    var dd_subject = new Object();
                    dd_subject.title = $(this).find('.ed.title-link').text()
                    dd_subject.link = this.attribs.href
                    if(!except_list.includes(dd_subject.link) && dd_subject.link != '#popup_menu_area'){
                        dd_list.push(dd_subject)
                    }
                })
            })
        }).then(()=>{
            console.log(dd_list)
            res.send(JSON.stringify(dd_list))
        })
    })
})

router.get('/hrm', function (req, res) {

    var except_list = []
    var hrm_list = []

    console.log("GET:hrm/Request for HRM Data")
    mysql.createConnection(db_config)
    .then(conn=>{
        var sql = "SELECT link FROM tb_link_info;"
        rows = conn.query(sql)
        return rows
    }).then(rows=>{
        for(var i = 0; i < rows.length; i++){
            except_list.push(rows[i].link)
        }
        puppeteer.launch()
        .then(browser=>{
            console.log("GET:hrm/Launch Puppeteer")
            return browser.newPage()
            .then(page=>{
                return page.goto('http://insagirl-toto.appspot.com/hrm/?where=2', {waitUntil:'networkidle2'})
                .then(() => page.$('#hrmbodyexpand'))
                .then((expand) => expand.click())
                .then(() => page.waitFor(1000))
                .then(() => page.$eval('#hrmbody', (element)=>{
                    console.log("GET:hrm/Getting Data from hrmbody..")
                    var links = Array.from(element.querySelectorAll('a'))
                    return links.map(link=>link.href)
                }))
                .then((data)=>{
                    for(var i = 0; i<data.length; i++){
                        if(!except_list.includes(data[i]) && !data[i].includes('dostream')){
                            hrm_list.push(data[i])
                        }
                    }
                })
            }).then(()=>browser.close())
        }).then(()=>{
            console.log(hrm_list)
            res.send(JSON.stringify(hrm_list))
        })
    })
})

module.exports = router;