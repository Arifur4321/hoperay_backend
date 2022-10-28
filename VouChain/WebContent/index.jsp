<link rel="stylesheet" type="text/css" href="css3clock.css" />
<h1 style="color: #5e9ca0; text-align: center;"><strong>Welcome to VouChain </strong></h1>
<h1 style="color: #5e9ca0; text-align: center;"><strong>Backend RestServices</strong></h1>
<p>&nbsp;</p>

<div align="center">
	<div id="liveclock" align="center" class="outer_face">
	
		<div class="marker oneseven" ></div>
		<div class="marker twoeight"></div>
		<div class="marker fourten"></div>
		<div class="marker fiveeleven"></div>
		
		<div class="inner_face">
			<div class="hand hour"></div>
			<div class="hand minute"></div>
			<div class="hand second"></div>
		</div>
	
	</div>
</div>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script type="text/javascript">

/***********************************************
* CSS3 Analog Clock- by JavaScript Kit (www.javascriptkit.com)
* Visit JavaScript Kit at http://www.javascriptkit.com/ for this script and 100s more
***********************************************/

var $hands = $('#liveclock div.hand')

window.requestAnimationFrame = window.requestAnimationFrame
                               || window.mozRequestAnimationFrame
                               || window.webkitRequestAnimationFrame
                               || window.msRequestAnimationFrame
                               || function(f){setTimeout(f, 60)}


function updateclock(){
	var curdate = new Date()
	var hour_as_degree = ( curdate.getHours() + curdate.getMinutes()/60 ) / 12 * 360
	var minute_as_degree = curdate.getMinutes() / 60 * 360
	var second_as_degree = ( curdate.getSeconds() + curdate.getMilliseconds()/1000 ) /60 * 360
	$hands.filter('.hour').css({transform: 'rotate(' + hour_as_degree + 'deg)' })
	$hands.filter('.minute').css({transform: 'rotate(' + minute_as_degree + 'deg)' })
	$hands.filter('.second').css({transform: 'rotate(' + second_as_degree + 'deg)' })
	requestAnimationFrame(updateclock)
}

requestAnimationFrame(updateclock)


</script>