// Obter ID do usuário atual do localStorage
let currentUser = localStorage.getItem('currentUser');
let editingTaskId = null;
let categories = [];

function apiCall(method, url, body) {
    const options = {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    };
    if (body) options.body = JSON.stringify(body);
    return fetch(url, options);
}

function loadCategories() {
    apiCall('GET', '/api/categories')
        .then(r => r.json())
        .then(data => {
            categories = data;
            const select = document.getElementById('newCategory');
            if (select) {
                select.innerHTML = '<option value="">Sem categoria</option>';
                data.forEach(c => {
                    const opt = document.createElement('option');
                    opt.value = c.id;
                    opt.textContent = c.name;
                    select.appendChild(opt);
                });
            }
        })
        .catch(e => console.error('Erro:', e));
}

function loadTasks() {
    if (!currentUser) return;
    apiCall('GET', '/api/tasks/user/' + currentUser)
        .then(r => r.json())
        .then(tasks => {
            const ul = document.getElementById('tasks');
            ul.innerHTML = '';
            if (!tasks || tasks.length === 0) {
                ul.innerHTML = '<li style="text-align:center;color:#999;">Nenhuma tarefa</li>';
                return;
            }
            tasks.forEach(task => {
                const li = document.createElement('li');
                li.className = 'task-item ' + (task.completed ? 'completed' : '');
                const cat = categories.find(c => c.id === task.categoryId);
                li.innerHTML = '<div class="task-content"><input type="checkbox" ' + (task.completed ? 'checked' : '') + ' onchange="toggleCompleted(' + task.id + ',' + task.completed + ')"/><div><h4>' + task.title + '</h4><p>' + (task.description || '') + '</p><span class="cat">' + (cat ? cat.name : 'Sem') + '</span></div></div><button onclick="editTask(' + task.id + ')">Editar</button><button onclick="deleteTask(' + task.id + ')">Excluir</button>';
                ul.appendChild(li);
            });
        })
        .catch(e => console.error('Erro:', e));
}

document.getElementById('addTask').onclick = function() {
    const title = document.getElementById('newTitle').value.trim();
    const desc = document.getElementById('newDesc').value.trim();
    const cat = document.getElementById('newCategory').value;
    const due = document.getElementById('newDue').value;
    if (!title) {
        alert('Digite um título');
        return;
    }
    const taskData = {
        title: title,
        description: desc,
        category_id: cat ? parseInt(cat) : null,
        due_date: due || null,
        completed: 0
    };
    if (editingTaskId) {
        apiCall('PUT', '/api/tasks/' + editingTaskId, taskData)
            .then(r => {
                if (r.ok) {
                    alert('Atualizado');
                    editingTaskId = null;
                    document.getElementById('addTask').textContent = 'Adicionar Tarefa';
                    clearForm();
                    loadTasks();
                }
            })
            .catch(e => alert('Erro'));
    } else {
        taskData.user_id = currentUser;
        apiCall('POST', '/api/tasks', taskData)
            .then(r => r.json().then(data => ({ok: r.ok, status: r.status, data: data})))
            .then(result => {
                if (result.status === 201) {
                    alert('Adicionado');
                    clearForm();
                    loadTasks();
                }
            })
            .catch(e => alert('Erro'));
    }
};

function editTask(id) {
    apiCall('GET', '/api/tasks/' + id)
        .then(r => r.json())
        .then(task => {
            document.getElementById('newTitle').value = task.title;
            document.getElementById('newDesc').value = task.description || '';
            document.getElementById('newCategory').value = task.categoryId || '';
            document.getElementById('newDue').value = task.dueDate || '';
            editingTaskId = id;
            document.getElementById('addTask').textContent = 'Salvar Tarefa';
        })
        .catch(e => alert('Erro'));
}

function deleteTask(id) {
    if (confirm('Excluir?')) {
        apiCall('DELETE', '/api/tasks/' + id)
            .then(r => {
                if (r.ok) {
                    loadTasks();
                }
            })
            .catch(e => alert('Erro'));
    }
}

function toggleCompleted(id, currentCompleted) {
    const newStatus = currentCompleted ? 0 : 1;
    apiCall('PATCH', '/api/tasks/' + id + '/completed', {completed: newStatus})
        .then(r => {
            if (r.ok) loadTasks();
        })
        .catch(e => console.error('Erro:', e));
}

function clearForm() {
    document.getElementById('newTitle').value = '';
    document.getElementById('newDesc').value = '';
    document.getElementById('newCategory').value = '';
    document.getElementById('newDue').value = '';
}

// Carregar categorias e tarefas ao iniciar
window.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    loadTasks();
});
