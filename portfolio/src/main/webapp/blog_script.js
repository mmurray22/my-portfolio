/*
 * This is the JavaScript file for 
 * the blog portion of the website
 */
google.charts.load('current', {'packages':['gantt']});

function drawChart() {
    var data = new google.visualization.DataTable();
    
    //Create columns
    data.addColumn('string', 'Title');
    data.addColumn('date', 'Start');
    data.addColumn('date', 'End');
    // data.addColumn('number', 'Duration');

    //Create rows
    data.addRows([
        ['Application Opens',
         new Date(2019, 8, 1), new Date(2019, 8, 1)],
        ['Application Working Period',
         new Date(2019, 8, 2), new Date(2019, 12, 31)],
        ['Applications Due',
         new new Date(2020, 1, 1), new Date(2020, 1, 1)], 
        ['Application Results Waiting Period',
         new new Date(2020, 1, 2), new Date(2020, 3, 31)],
        ['Applications Results Release',
         new new Date(2020, 4, 1), new Date(2020, 4, 1)],
    ]);

    var options = {
        height: 275
    };
    var chart = new google.visualization.Gantt(document.getElementById('chart'));
    chart.draw(data, options);
}

function returnHome() {
   location.replace("/index.html");
   console.log(`Return Home`);
}
