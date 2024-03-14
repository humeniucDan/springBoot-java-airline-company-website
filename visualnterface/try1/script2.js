const currentPort = '8000';

const claseSubelem = "sublist-elem w3-round-xxlarge";

function getPlaneData(id) {
    console.log(id);
    fetch('http://localhost:' + currentPort + '/pilotiAvion', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: id
    })
        .then(res => res.json())
        .then(data => {
            console.log(data);

            document.getElementById('titluPiloteaza').innerHTML = "Pilotata de";

            const list = document.getElementById('listaPiloteaza');
            list.innerHTML = [];

            data.forEach(an => {

                const newLine = `<li class="pilot-nh sublist-elem">${an.numean}</li>`;
                list.innerHTML += newLine;
            })
        })
        .catch(error => console.log(error));
}

fetch('http://localhost:' + currentPort + '/avioane')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('listaAngajati');  
        list.innerHTML = [];

        data.forEach(av => {

            var newLine;
            newLine = `<li class="plane-nh"> <div onclick="getPlaneData(${av.id})" 
            class="plane-nr ${claseSubelem}"> ${av.numeav}</div>: &nbsp ${av.nr_pil}
            <img src="ico/pilot.svg" /></li>`;


            list.innerHTML += newLine;

        })
    })

form.addEventListener('submit', event => {
    event.preventDefault();

    const formData = new FormData(form);
    const data = Object.fromEntries(formData);

    fetch('http://localhost:' + currentPort + '/avioaneSpeciale', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: data.name
    })
        .then(res => res.json())
        .then(data => {
            console.log(data);

            const list = document.getElementById('listaAngajati');
            list.innerHTML = [];

            data.forEach(av => {

                var newLine;
                newLine = `<li onclick="getPlaneData(${av.id})" class="plane ${claseSubelem}">${av.numeav}</li>`;

                list.innerHTML += newLine;

            })
        })
        .catch(error => console.log(error));
    fetch('http://localhost:' + currentPort + '/5a', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: data.name
    })
        .then(res => res.json())
        .then(data => {
            console.log(data);

            const list = document.getElementById('salMax');
            list.innerHTML = [];

            data.forEach(av => {

                var newLine;
                newLine = `Salariu maxim: ${av.salMax}`;

                list.innerHTML += newLine;

            })
        })
        .catch(error => console.log(error));
});

