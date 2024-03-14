const currentPort = '8000';

function getPilotData(id){
    fetch('http://localhost:' + currentPort + '/avioanePilot', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: id
    })
        .then(res => res.json())
        .then(data => {
            console.log(data);

            document.getElementById('titluPiloteaza').innerHTML = "Piloteaza";

            const list = document.getElementById('listaPiloteaza');
            list.innerHTML = [];

            data.forEach(av => {

                const newLine = `<li class="plane-nh sublist-elem">${av.numeav}</li>`;
                list.innerHTML += newLine;
            })
        })
        .catch(error => console.log(error));
}

fetch('http://localhost:' + currentPort + '/angajati')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('listaAngajati');
        const claseSubelem = "sublist-elem w3-round-xxlarge";  
        list.innerHTML = [];

        data.forEach(an => {

            var newLine;

            switch (an.functie) {
                case "Pilot":
                    newLine = `<li onclick=getPilotData(${an.id}) class="${claseSubelem} pilot">${an.numean}</li>`;
                    break;
                case "Cabin Crew":
                    newLine = `<li class="attendant ${claseSubelem}">${an.numean}</li>`;
                    break;
                case "Director":
                    newLine = `<li class="ceo ${claseSubelem}">${an.numean}</li>`;
                    break;
                default:
                    newLine = `<li class="manager ${claseSubelem}">${an.numean}</li>`;
            }

            list.innerHTML += newLine;

        })
    })
fetch('http://localhost:' + currentPort + '/6b')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('listaSalMed');
        const claseSubelem = "sublist-elem w3-round-xxlarge";
        list.innerHTML = [];

        data.forEach(an => {

            var newLine;

            switch (an.functie) {
                case "Pilot":
                    newLine = `<li class="pilot-nh ${claseSubelem}">${an.salariu}</li>`;
                    break;
                case "Cabin Crew":
                    newLine = `<li class="attendant ${claseSubelem}">${an.salariu}</li>`;
                    break;
                case "Director":
                    newLine = `<li class="ceo ${claseSubelem}">${an.salariu}</li>`;
                    break;
                default:
                    newLine = `<li class="manager ${claseSubelem}">${an.salariu}</li>`;
            }

            list.innerHTML += newLine;

        })
    })

