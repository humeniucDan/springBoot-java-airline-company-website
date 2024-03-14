const currentPort = '8000'

fetch('http://localhost:' + currentPort + '/3a')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('3a');

        data.forEach(an => {

            var newLine = `<li> ${an.de_la}->${an.la}, ${an.zi} </li>`

            list.innerHTML += newLine;

        })
    })
fetch('http://localhost:' + currentPort + '/3b')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('3b');

        data.forEach(an => {

            var newLine = `<li> ${an.de_la}->${an.la}, ${an.zi} </li>`

            list.innerHTML += newLine;

        })
    })
fetch('http://localhost:' + currentPort + '/4a')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('4a');

        data.forEach(an => {

            var newLine = `<li> ${an.de_la}->${an.la}, ${an.zi} </li>`

            list.innerHTML += newLine;

        })
    })
fetch('http://localhost:' + currentPort + '/4b')
    .then(res => {
        console.log(res);
        return res.json();
    })
    .then(data => {
        const list = document.getElementById('4b');

        data.forEach(an => {

            var newLine = `<li> ${an.av1} si ${an.av2} </li>`

            list.innerHTML += newLine;

        })
    })

