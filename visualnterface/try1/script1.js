const currentPort = '8000'

var curDate = new Date();

var year = curDate.getFullYear();
var month = curDate.getMonth() + 1;
if(month < 10) month = '0' + month;
var day = curDate.getDate();
if (day < 10) day = '0' + day;

var hour = curDate.getHours();
if (hour < 10) hour = '0' + hour;
var minutes = curDate.getMinutes();
if (minutes < 10) minutes = '0' + minutes;

document.getElementById("formDate").defaultValue =
    year + "-" + month + "-" + day;
document.getElementById("formTime").defaultValue =
    hour + ':' + minutes;   

//Afisare zboruri
const form = document.querySelector('.form');

form.addEventListener('submit', event => {
    event.preventDefault();

    const formData = new FormData(form);
    const data = Object.fromEntries(formData);

    data.weekDay = weekDay(data.date) + "";
    console.log(JSON.stringify(data));

    fetch('http://localhost:' + currentPort + '/zboruri', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
        .then(res => res.json())
        .then(data => {
            console.log(data);

            const list = document.getElementById('listaZboruri');
            list.innerHTML = [];

            data.forEach(ruta => {

                const rs = ruta[0], rf = ruta[ruta.length - 1];
                var newLine =
                    //${an.numean} are functia de ${an.functie}
                    `<li> <div class="hover-trigger"> ${rs.from}, ${rs.leave} --> ${rf.to}, ${rf.arrive} </div> <ul class="hover-content lz-se">`;
                ruta.forEach(z => {
                    newLine += `<li> ${z.from}, ${z.leave} --> ${z.to}, ${z.arrive} </li>`;
                })

                newLine += `</ul></li>`;
                list.innerHTML += newLine;
            })
            list.innerHTML += `<li></li>`;
        })
        .catch(error => console.log(error));
});

function weekDay(dateString) {
  let dateParts = dateString.split('-');
  return (new Date(dateParts[0], dateParts[1] - 1, dateParts[2]).getDay() + 6) % 7;
}


