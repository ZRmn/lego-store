$(document).ready(function(){
  let params = gerUrlParams();

  let priceFrom = 0;
  let priceTo = 2000;
  let piecesFrom = 0;
  let piecesTo = 8000;
  let yearFrom = 2010;
  let yearTo = 2020;

  if (params){
    if (params.priceFrom)
    {
      //document.forms.filtersForm.elements.priceFrom.value = params.priceFrom;
      priceFrom = params.priceFrom;
    }
    if (params.priceTo)
    {
      //document.forms.filtersForm.elements.priceTo.value = params.priceTo;
      priceTo = params.priceTo;
    }
    if (params.piecesFrom)
    {
      //document.forms.filtersForm.elements.piecesFrom.value = params.piecesFrom;
      piecesFrom = params.piecesFrom;
    }
    if (params.piecesTo)
    {
      //document.forms.filtersForm.elements.piecesTo.value = params.piecesTo;
      piecesTo = params.piecesTo;
    }
    if (params.yearFrom)
    {
      //document.forms.filtersForm.elements.yearFrom.value = params.yearFrom;
      yearFrom = params.yearFrom
    }
    if (params.yearTo)
    {
      //document.forms.filtersForm.elements.yearTo.value = params.yearTo;
      yearTo = params.yearTo
    }
  }

  $( "#slider-range-price" ).slider({
    range: true,
    min: 0,
    max: 2000,
    values: [ priceFrom, priceTo ],
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
    values: [ piecesFrom, piecesTo ],
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
    values: [ yearFrom, yearTo ],
    slide: function( event, ui ) {
      $( "#year_amount" ).val(ui.values[ 0 ] + " - " + ui.values[ 1 ] );
      $("#yearFrom").val(ui.values[0]);
      $("#yearTo").val(ui.values[1]);
    }
  });
  $( "#year_amount" ).val($( "#slider-range-year" ).slider( "values", 0 ) +
      " - " + $( "#slider-range-year" ).slider( "values", 1 ) );
});