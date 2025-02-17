const url = 'http://localhost:8081/admin/users';
const urlRoles = 'http://localhost:8081/admin/roles';
const container = document.querySelector('.usersTbody');
const newUserForm = document.getElementById('newUserForm');
const editUserForm = document.getElementById('editUserForm');
const deleteUserForm = document.getElementById('deleteUserForm');
const btnCreate = document.getElementById('new-user-tab');
const newRoles = document.getElementById('newRoles');
let result = '';

var editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
var deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
const editId = document.getElementById('editId');
const editFirstName = document.getElementById('editFirstName');
const editLastName = document.getElementById('editLastName');
const editAge = document.getElementById('editAge');
const editEmail = document.getElementById('editEmail');
const editPassword = document.getElementById('editPassword');
const editRoles = document.getElementById('editRoles');

const delId = document.getElementById('delId');
const delFirstName = document.getElementById('delFirstName');
const delLastName = document.getElementById('delLastName');
const delAge = document.getElementById('delAge');
const delEmail = document.getElementById('delEmail');
const delRoles = document.getElementById('delRoles');

const newFirstName = document.getElementById('newFirstName');
const newLastName = document.getElementById('newLastName');
const newAge = document.getElementById('newAge');
const newEmail = document.getElementById('newEmail');
const newPassword = document.getElementById('newPassword');

let rolesArr = [];

const renderUsers = (users) => {
    result = ''; // Очистите результат перед добавлением новых данных
    users.forEach(user => {
        let roles = '';
        user.roles.forEach(role => {
            roles += role.name.substring(5) + ' ';
        });
        result += `
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${roles}</td>
                <td><a class="btnEdit btn btn-sm btn-info text-white">Edit</a></td>
                <td><a class="btnDelete btn btn-danger btn-sm">Delete</a></td>
            </tr>
        `;
    });
    container.innerHTML = result;
};

const renderRoles = (roles) => {
    let rolesOptions = '';
    roles.forEach(role => {
        rolesOptions += `<option value="${role.id}">${role.name.substring(5)}</option>`;
        rolesArr.push(role);
    });
    newRoles.innerHTML = rolesOptions;
    editRoles.innerHTML = rolesOptions;
    delRoles.innerHTML = rolesOptions;
};

fetch(url)
    .then(res => {
        if (!res.ok) {
            throw new Error('Network response was not ok');
        }
        return res.json();
    })
    .then(data => {
        console.log('Data received:', data);
        if (!Array.isArray(data)) {
            throw new Error('Expected an array of users');
        }
        renderUsers(data);
    })
    .catch(error => console.error('Failed to fetch users:', error));

fetch(urlRoles)
    .then(res => {
        if (!res.ok) {
            throw new Error('Network response was not ok');
        }
        return res.json();
    })
    .then(data => {
        console.log('Roles received:', data);
        if (!Array.isArray(data)) {
            throw new Error('Expected an array of roles');
        }
        renderRoles(data);
    })
    .catch(error => console.error('Failed to fetch roles:', error));

const refreshListOfUsers = () => {
    fetch(url)
        .then(res => {
            if (!res.ok) {
                throw new Error('Network response was not ok');
            }
            return res.json();
        })
        .then(data => {
            console.log('Refreshing user list:', data);
            if (!Array.isArray(data)) {
                throw new Error('Expected an array of users');
            }
            result = '';
            renderUsers(data);
        })
        .catch(error => console.error('Failed to fetch users:', error));
};

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e);
        }
    });
};

on(document, 'click', '.btnDelete', e => {
    const row = e.target.closest('tr');
    const id = row.cells[0].innerHTML;
    const firstName = row.cells[1].innerHTML;
    const lastName = row.cells[2].innerHTML;
    const age = row.cells[3].innerHTML;
    const email = row.cells[4].innerHTML;

    delId.value = id;
    delFirstName.value = firstName;
    delLastName.value = lastName;
    delAge.value = age;
    delEmail.value = email;
    deleteUserModal.show();
});

let idForm = 0;
on(document, 'click', '.btnEdit', e => {
    const row = e.target.closest('tr');
    const id = row.cells[0].innerHTML;
    const firstName = row.cells[1].innerHTML;
    const lastName = row.cells[2].innerHTML;
    const age = row.cells[3].innerHTML;
    const email = row.cells[4].innerHTML;

    editId.value = id;
    editFirstName.value = firstName;
    editLastName.value = lastName;
    editAge.value = age;
    editEmail.value = email;
    editPassword.value = '';
    editRoles.options.selectedIndex = -1;
    editUserModal.show();
});

btnCreate.addEventListener('click', () => {
    newFirstName.value = '';
    newLastName.value = '';
    newAge.value = '';
    newEmail.value = '';
    newPassword.value = '';
    newRoles.options.selectedIndex = -1;
});

deleteUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    fetch(url + '/' + delId.value, {
        method: 'DELETE',
        headers: {
            'Content-type': 'application/json'
        },
    })
        .then(res => {
            if (res.ok) {
                console.log('User deleted successfully');
                return res.text(); // Используем res.text() для получения текста ответа
            } else {
                throw new Error('Failed to delete user');
            }
        })
        .then(text => {
            console.log('Response text:', text);
            // Обновляем список пользователей
            refreshListOfUsers();
        })
        .then(() => {
            // Закрываем модальное окно после обновления списка
            deleteUserModal.hide();
        })
        .catch(err => {
            console.error('Error:', err.message);
            alert('Ошибка при удалении пользователя: ' + err.message);
        });
});


newUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const selectedOpts = [...newRoles.options].filter(x => x.selected).map(x => x.value);
    const rolesJ = selectedOpts.map(role => rolesArr[role - 1]);

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: newFirstName.value,
            lastName: newLastName.value,
            age: newAge.value,
            email: newEmail.value,
            password: newPassword.value,
            roles: rolesJ
        })
    })
        .then(res => {
            if (res.ok) {
                return res.json();
            } else {
                throw new Error('Failed to create user');
            }
        })
        .then(() => {
            // Очистка формы
            newFirstName.value = '';
            newLastName.value = '';
            newAge.value = '';
            newEmail.value = '';
            newPassword.value = '';
            newRoles.options.selectedIndex = -1;

            // Обновление списка пользователей
            refreshListOfUsers();

            // Переключение на вкладку "All Users"
            const allUsersTab = new bootstrap.Tab(document.getElementById('user-table-tab'));
            allUsersTab.show(); // Активируем вкладку "All Users"
        })
        .catch(err => {
            console.error('Error:', err.message);
            alert('Ошибка при создании пользователя: ' + err.message);
        });
});

editUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const selectedOpts = [...editRoles.options].filter(x => x.selected).map(x => x.value);
    const rolesJ = selectedOpts.map(role => rolesArr[role - 1]);

    fetch(url + '/' + editId.value, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: editId.value,
            firstName: editFirstName.value,
            lastName: editLastName.value,
            age: editAge.value,
            email: editEmail.value,
            password: editPassword.value,
            roles: rolesJ
        })
    })
        .then(res => {
            if (res.ok) {
                return res.json();
            } else {
                throw new Error('Failed to edit user');
            }
        })
        .then(refreshListOfUsers)
        .catch(err => console.log(err));
    editUserModal.hide();
});