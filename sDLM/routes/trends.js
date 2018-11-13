var express = require('express');
var request = require('request');
var cheerio = require('cheerio');
var router = express.Router();
var num_search_page = 3
var bp_board_humor = 'http://v12.battlepage.com/??=Board.Humor.Table';
var bp_board_etc = 'http://v12.battlepage.com/??=Board.ETC.Table';


router.get('/', function(req, res){
    //res.send('Trends Check API Page')



/*

    for(var i = 1; i<=num_search_page; i++){
        request.get({url:bp_board_humor+'&page='+i},  function(err, response, body){
            var $ = cheerio.load(body);
            var $table_data = $('#div_content_containter > div:nth-child(2) > div.detail_container > div.ListTable');
            $table_data.find('a').each(function(index, element){
                bp_list.push($(element).attr('href'));
                console.log(bp_list)
            })
        })  
    }

    res.send(bp_list)
*/



});



router.get('/bp', function(req, res){

})
router.get('/dd', function(req, res){

})
module.exports = router;