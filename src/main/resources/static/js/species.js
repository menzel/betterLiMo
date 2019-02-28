
// Species model ///////////////////

var c = 0;
var cols = ['#A52A2A',
    '#5F9EA0',
    '#D2691E',
    '#f4c742',
    '#006400',
    '#9932CC',
    '#00ad0d',
    '#000080'];


function nextColor() {
    if (c < cols.length){
        return cols[c++];
    }

    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}


var species = [];
var current = -1;
var nextid = 0;
fills = [];

function addSpecies() {

    var e = document.getElementById("speciesselect");
    var name = e.options[e.selectedIndex].text;

    addSpeciesToTable(name);
}

function getkind(name) {

    for(var key in lichen){
        if(lichen[key].includes(name)) return key;
    }

    return "Unbekannt";
}

function addSpeciesToTable(name) {

    var spec = {};
    spec.name = name;
    spec.kind = getkind(name);
    spec.color = nextColor();
    spec.id = nextid;
    nextid++;

    species.push(spec);

    current = species.length - 1;
    displaySpecies();
}

function addArea(px, id) {
    var area = px/factor;

    fills.push([id, area]);
    displaySpecies();
}

function getCurrentColor(){

    // this is the first function called upon click, so init a new species here
    if(current === -1){
        addSpeciesToTable("Unbekannt");
    }

    return species[current].color;
}

function undoAddArea() {
    fills.splice(-1,1); // removes last element
    displaySpecies();

}

// Species table left functions ///////////////////

function getthallicount(id) {
     var total = 0;

    for(var i = 0 ; i < fills.length; i++){
        if(fills[i][0] === id){
            total++;
        }
    }

    return total;
}

function getarea(id) {

    var total = 0;

    for(var i = 0 ; i < fills.length; i++){
        if(fills[i][0] === id){
            total += fills[i][1];

        }
    }

    return Math.round(total);
}


// Species table left///////////////////

function displaySpecies() {
    var spec_table = document.getElementById("species");
    spec_table.getElementsByTagName('tbody')[0].innerHTML = "";
    var body = spec_table.getElementsByTagName('tbody')[0];

    for(var i = 0; i < species.length; i++) {

        var row = body.insertRow(0);
        row.classList.add("clickablerow");
        row.id = species[i].id;

        var color = row.insertCell(0);
        var area = row.insertCell(0);
        var name = row.insertCell(0);

        name.innerText = species[i].name;
        area.innerText = getarea(species[i].id);
        color.style.backgroundColor = species[i].color;

        if (i === current) {
            row.style.backgroundColor = "#5d97aa";
        }
    }

    // select species from table
    jQuery(document).ready(function($) {
        $(".clickablerow").click(function() {

            var id = $(this).attr("id");
            current = parseInt(id); // current is equivalent to the position in the species list, which is the same as the id

            displaySpecies(); // speed up by only change background colors
        });
    });

}


// Create results file ///////////////////

function generateCsv(){
    var results = [];
    results.push("id;Gattung;Art;Fläche [mm^2]; Fläche [%]; Anzahl Thalli; Farbe [RGB]");

    for(var i = 0; i < species.length; i++) {
        var tmp = "";
        var area = getarea(species[i].id);
        var count = getthallicount(species[i].id);

        var kuerzel =  species[i].name.split(",")[1];
        var name =  species[i].name.split(",")[0];


        tmp += species[i].id;
        tmp += ";";
        tmp += kuerzel;
        tmp += ";";
        tmp += name;
        tmp += ";";
        tmp += species[i].kind;
        tmp += ";";
        tmp += area;
        tmp += ";";
        tmp += 100*area/(200*200);
        tmp += ";";
        tmp += count;
        tmp += ";";
        tmp += species[i].color.substring(1);
        tmp += ";";

        results.push(tmp);
    }

    return results.join("%0A%0D");

}

function getExcel(filename){

    let dat = [];
    dat.push([
        {
        value: "ID",
        type: "string"
        }, {
        value: "Kürzel",
        type: "string"
        },{
        value: "Gattung",
        type: "string"
        }, {
        value: "Art",
        type: "string"
        },{
        value: "Fläche [mm^2]",
        type: "string"
        },{
        value: "Fläche [%]",
        type: "string"
        },{
        value: "Anzahl Thalli",
        type: "string"
        },{
        value: "Farbe [RGB]",
        type: "string"
        }
    ]);

        for(var i = 0; i < species.length; i++) {
            var area = getarea(species[i].id);
            var count = getthallicount(species[i].id);
            var kuerzel =  species[i].name.split(",")[1];
            var name =  species[i].name.split(",")[0];

            dat.push([
                {
                    value: species[i].id,
                    type: "number"
                },{
                    value: kuerzel,
                    type: "string"
                }, {
                    value: name,
                    type: "string"
                },{
                    value: species[i].kind,
                    type: "string"
                },{
                    value: area,
                    type: "number"
                },{
                    value: 100*area/(200*200),
                    type: "number"
                },{
                    value: count,
                    type: "number"
                },{
                    value: species[i].color.substring(1),
                    type: "string"
                }
            ]);
        }

    filename = filename.replace(/\.?jpg|\.?csv/g,"");

    const config = {
        filename: filename,
        sheet: {
            data: dat
        }
    };

    zipcelx(config);
}



// Results download ///////////////////

function getCsv(filename) {
    var element = document.createElement('a');
    filename = filename.replace(/\.?jpg/g,"");
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + generateCsv(filename));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}
