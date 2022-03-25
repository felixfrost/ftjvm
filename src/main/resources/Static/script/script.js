$(document).ready(function() {

  let progress = $('.progressbar .progress')

  function counterInit( fValue, lValue ) {

    let counter_value = parseInt( $('.counter').text() );
    counter_value++;

    if( counter_value >= fValue && counter_value <= lValue ) {

      $('.counter').text( counter_value + '%' );
      progress.css({ 'width': counter_value + '%' });

      setTimeout( function() {
        counterInit( fValue, lValue );
      }, 100 );


    }

  }

  counterInit( 0, 100 );

});