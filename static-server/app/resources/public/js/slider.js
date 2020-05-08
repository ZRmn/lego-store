$(document).ready(function(){
  $( "#slider-range-price" ).slider({
    range: true,
    min: 75,
    max: 500,
    values: [ 75, 300 ],
    slide: function( event, ui ) {
      $( "#price_amount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
    }
  });
  $( "#price_amount" ).val( "$" + $( "#slider-range-price" ).slider( "values", 0 ) +
    " - $" + $( "#slider-range-price" ).slider( "values", 1 ) );


  $( "#slider-range-pieces" ).slider({
    range: true,
    min: 500,
    max: 3000,
    values: [ 500, 3000 ],
    slide: function( event, ui ) {
      $( "#pieces_amount" ).val(ui.values[ 0 ] + " шт - " + ui.values[ 1 ] + " шт" );
    }
  });
  $( "#pieces_amount" ).val($( "#slider-range-pieces" ).slider( "values", 0 ) + " шт" +
    " - " + $( "#slider-range-pieces" ).slider( "values", 1 ) + " шт" );


  $( "#slider-range-year" ).slider({
    range: true,
    min: 2010,
    max: 2020,
    values: [ 2010, 2020 ],
    slide: function( event, ui ) {
      $( "#year_amount" ).val(ui.values[ 0 ] + " - " + ui.values[ 1 ] );
    }
  });
  $( "#year_amount" ).val($( "#slider-range-year" ).slider( "values", 0 ) +
    " - " + $( "#slider-range-year" ).slider( "values", 1 ) );
});

