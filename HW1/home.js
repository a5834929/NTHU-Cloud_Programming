var state = 0;
$(function(){
	initToolTip();
	imageModal();
	captionShow();
});

function initToolTip(){
	$('[data-toggle="tooltip"]').tooltip();
}

function imageModal(){
	
	$("img").click(function(){
		if(state===0){
			var src = $(this).attr('src');
			var width = $(this).width();
			var height = $(this).height();

			$(".modal-content > img").attr('src', src);

			var ratio = height / width;
			if(width>=900 && ratio<=1){
				width = 900;
				height = width*ratio;
				$(".modal-content > img").attr('width', '900px');
				$(".modal-content > img").attr('left', 'cal(50% - 450px)');
				$(".modal-content > img").attr('height', height+'px');

			}else if(height>=440){
				height = 440;
				width = height/ratio;
				$(".modal-content > img").attr('width', width+'px');
				$(".modal-content > img").attr('left', 'cal(50% - '+(width/2)+'px)');
				$(".modal-content > img").attr('height', '440px');
			}
			state = 1;
		}
		else state = 0;
		$(".modelBtn").click();
	});
}

function captionShow(){
	$(".thumbnail").hover(
		function(){
			$(this).children('.caption').css('height', '25%');
			$(this).children('.caption').css('opacity', '0.8');
		},
		function(){
			if($(this).find('input:checked').length === 0){
				$(this).children('.caption').css('height', '0%');
				$(this).children('.caption').css('opacity', '0');
			}
		}
	);

}


