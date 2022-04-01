$(document).ready(function() {

  let progress = $('.progressbar .progress')

  function counterInit( startTime, endTime ) {

    let counter_value = parseInt( $('.counter').text() );
    counter_value++;

    if( counter_value >= startTime && counter_value <= endTime ) {

      $('.counter').text( counter_value + '%' );
      progress.css({ 'width': counter_value + '%' });

//millisekund tills den blir större
      setTimeout( function() {
        counterInit( startTime, endTime );
      }, 100 );

    }
   if (counter_value > endTime) {
        document.forms["quizForm"].submit()
   }

  }

  counterInit( 0, 100 );

});