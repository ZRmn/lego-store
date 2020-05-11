$(document).ready(function(){
  $( "#slider-range-price" ).slider({
    range: true,
    min: 0,
    max: 2000,
    values: [ 0, 2000 ],
    slide: function( event, ui ) {
      $( "#price_amount" ).val(ui.values[ 0 ] + " р" + " - " + ui.values[ 1 ] + " р" );
      $("#priceFrom").val(ui.values[0]);
      $("#priceTo").val(ui.values[1]);
    }
  });
  $( "#price_amount" ).val( $( "#slider-range-price" ).slider( "values", 0 )  + " р"+
    " - " + $( "#slider-range-price" ).slider( "values", 1 ) + " р" )

  $( "#slider-range-pieces" ).slider({
    range: true,
    min: 0,
    max: 8000,
    values: [ 0, 8000 ],
    slide: function( event, ui ) {
      $( "#pieces_amount" ).val(ui.values[ 0 ] + " шт - " + ui.values[ 1 ] + " шт" );
      $("#piecesFrom").val(ui.values[0]);
      $("#piecesTo").val(ui.values[1]);
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
      $("#yearFrom").val(ui.values[0]);
      $("#yearTo").val(ui.values[1]);
    }
  });
  $( "#year_amount" ).val($( "#slider-range-year" ).slider( "values", 0 ) +
    " - " + $( "#slider-range-year" ).slider( "values", 1 ) );
});

function setPriceFrom(priceFrom)
{
  $("#slider-range-price").slider("values", 0, 100);
}