document.getElementById('addNewUser').addEventListener('click', createUser)

async function createUser() {
    const inputName = document.getElementById('newName').value;
    const inputLastName = document.getElementById('newLastName').value;
    const inputAge = document.getElementById('newAge').value;
    const inputEmail = document.getElementById('newEmail').value;
    const inputPassword = document.getElementById('newPassword').value;

    const name = inputName;
    const lastname = inputLastName;
    const age = inputAge;
    const email = inputEmail;
    const password = inputPassword;

    const listRoles = Array.from(document.getElementById('newRoles').options)
        .filter(option => option.selected)
        .map(option => ({
            id: option.value,
            name: `ROLE_${option.text}`,
        }));

    if (name && lastname && age && email && password && listRoles.length !== 0) {
        const res = await fetch("http://localhost:8080/api", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({name, lastname, age, email, password, roles: listRoles})
        });
        const result = await res.json();
        addUser(result);
    }

    document.getElementById('newName').value = '';
    document.getElementById('newLastName').value = '';
    document.getElementById('newAge').value = '';
    document.getElementById('newEmail').value = '';
    document.getElementById('newPassword').value = '';
}

let roleArray = (options) => {
    let array = []
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            let role = {
                id: options[i].value,
                name: options[i].text,
            }
            array.push(role)
        }
    }
    return array
}