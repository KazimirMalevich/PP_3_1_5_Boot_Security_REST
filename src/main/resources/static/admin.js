const URLAdminInfo = 'http://localhost:8080/api/auth';
const adminInfo = document.getElementById('adminInfo');
const tableUserAdmin = document.getElementById('tableAdmin');

function getCurrentAdmin() {
    fetch(URLAdminInfo)
        .then((res) => res.json())
        .then((userAdmin) => {
            let rolesStringAdmin = rolesToStringForAdmin(userAdmin.roles);
            let dataAdmin = '';
            adminInfo.innerHTML = `<h5>
            <span>${userAdmin.email} </span>
            <span> with roles: </span>
            <span>${rolesStringAdmin}</span>
                                  </h5>`;
            dataAdmin += `<tr>
            <td>${userAdmin.id}</td>
            <td>${userAdmin.username}</td>
           <td>${userAdmin.surname}</td>
             <td>${userAdmin.age}</td>
            <td>${userAdmin.email}</td>
             <td>${rolesStringAdmin}</td>
           </tr>`;
            tableUserAdmin.innerHTML = dataAdmin;
        });
}

getCurrentAdmin()

function rolesToStringForAdmin(roles) {
    let rolesString = '';
    for (const element of roles) {
        rolesString += (element.name.toString().replace('ROLE_', '') + ', ');
    }
    rolesString = rolesString.substring(0, rolesString.length - 2);
    return rolesString;
}

let formDelete = document.forms["formDelete"]
deleteUser();

async function deleteModal(id) {
    const modalDelete = new bootstrap.Modal(document.querySelector('#deleteModal'));
    await modal(formDelete, modalDelete, id);
}

function deleteUser() {
    formDelete.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("http://localhost:8080/api/users/" + formDelete.id.value, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
            $('#deleteClose').click()
            getAllUsers();

        });
    });
}

let formEdit = document.forms["formEdit"];
editUser();

const URLEdit = "http://localhost:8080/api/users/";

async function editModal(id) {
    const modalEdit = new bootstrap.Modal(document.querySelector('#editModal'));
    await modal(formEdit, modalEdit, id);
    loadRolesForEdit();
}

function editUser() {
    formEdit.addEventListener("submit", ev => {
        ev.preventDefault();

        let rolesForEdit = [];
        for (let i = 0; i < formEdit.roles.options.length; i++) {
            if (formEdit.roles.options[i].selected) rolesForEdit.push({
                id: formEdit.roles.options[i].value,
                name: "ROLE_" + formEdit.roles.options[i].text
            });
        }
        fetch(URLEdit, {
            method: "PATCH",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formEdit.id.value,
                username: formEdit.username.value,
                surname: formEdit.surname.value,
                age: formEdit.age.value,
                email: formEdit.email.value,
                password: formEdit.password.value,
                roles: rolesForEdit
            })
        }).then(() => {
            $('#editClose').click();
            getAllUsers();
        });
    });
}

function loadRolesForEdit() {
    let selectEdit = document.getElementById("edit-roles");
    selectEdit.innerHTML = "";
    fetch("http://localhost:8080/api/users/roles/")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.name.toString().replace('ROLE_', '');
                selectEdit.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRolesForEdit);

let formNew = document.forms["formNew"];
addUser();
const URLNew = "http://localhost:8080/api/users";

function addUser() {
    formNew.addEventListener("submit", ev => {
        ev.preventDefault();
        let rolesForNew = [];
        for (let i = 0; i < formNew.roles.options.length; i++) {
            if (formNew.roles.options[i].selected) rolesForNew.push({
                id: formNew.roles.options[i].value,
                name: "ROLE_" + formNew.roles.options[i].text
            });
        }
        fetch(URLNew, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: formNew.id.value,
                username: formNew.username.value,
                surname: formNew.surname.value,
                age: formNew.age.value,
                email: formNew.email.value,
                password: formNew.password.value,
                roles: rolesForNew
            })
        }).then(() => {
            formNew.reset();
            document.getElementById('nav-home-tab').click();
            getAllUsers();
        });
    });
}

function loadRolesForNew() {
    let selectNew = document.getElementById("create-roles");
    selectNew.innerHTML = "";
    fetch("http://localhost:8080/api/users/roles/")
        .then(res => res.json())
        .then(data => {
            data.forEach(role => {
                let option = document.createElement("option");
                option.value = role.id;
                option.text = role.name.toString().replace('ROLE_', '');
                selectNew.appendChild(option);
            });
        })
        .catch(error => console.error(error));
}

window.addEventListener("load", loadRolesForNew);

async function getUserById(id) {
    let response = await fetch("http://localhost:8080/api/users/" + id);
    return await response.json();
}

async function modal(form, modal, id) {
    modal.show();
    let user = await getUserById(id);
    let rolesStringAdmin = rolesToStringForAdmin(user.roles);
    form.id.value = user.id;
    form.username.value = user.username;
    form.surname.value = user.surname;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = user.password;
    form.roles.value = rolesStringAdmin;
}

const URLTableUsers = 'http://localhost:8080/api/users';
getAllUsers();

function getAllUsers() {
    fetch(URLTableUsers)
        .then(function (response) {
            return response.json();
        })
        .then(function (users) {
            let dataOfUsers = '';
            let rolesString = ''; // Здесь будет результат функции rolesToString
            const tableUsers = document.getElementById('tableUsers');
            for (let user of users) {
                rolesString = rolesToString(user.roles);
                dataOfUsers += `<tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.surname}</td>
                        <td>${user.age}</td>
                        <td>${user.email}</td>
                        <td>${rolesString}</td>
                        <td>
                          <button type="button"
                          class="btn btn-primary"
                          data-bs-toogle="modal"
                          data-bs-target="#editModal"
                          onclick="editModal(${user.id})">
                                Edit
                            </button>
                        </td>                  
                              <td>
                          <button type="button"
                          class="btn btn-danger"
                          data-bs-toogle="modal"
                          data-bs-target="#deleteModal"
                          onclick="deleteModal(${user.id})">
                                Delete
                            </button>
                        </td>
                    </tr>`;
            }
            tableUsers.innerHTML = dataOfUsers;
        })
}

function rolesToString(roles) {
    let rolesString = '';
    for (const element of roles) {
        rolesString += (element.name.toString().replace('ROLE_', '') + ', ');
    }
    rolesString = rolesString.substring(0, rolesString.length - 2);
    return rolesString;
}
