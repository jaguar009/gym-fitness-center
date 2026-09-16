document.addEventListener('DOMContentLoaded', () => {
  const path = location.pathname;
  const icons = {
    dashboard: '<path d="m3 10 9-7 9 7v10H3zM9 20v-7h6v7"/>',
    members: '<circle cx="9" cy="7" r="3"/><path d="M3 21v-3a6 6 0 0 1 12 0v3M16 4a3 3 0 0 1 0 6M18 14a5 5 0 0 1 3 4v3"/>',
    memberships: '<rect x="3" y="4" width="18" height="16" rx="2"/><path d="M3 9h18M7 15h4"/>',
    payments: '<rect x="3" y="5" width="18" height="14" rx="2"/><path d="M3 10h18M7 15h3"/>',
    attendance: '<rect x="3" y="5" width="18" height="16" rx="2"/><path d="M7 3v4M17 3v4M3 11h18m-13 5 3 2 5-4"/>',
    classes: '<path d="M3 9v6M6 6v12M6 12h12M18 6v12M21 9v6"/>',
    reports: '<path d="M4 20V12h4v8M10 20V7h4v13M16 20V3h4v17"/>',
    employees: '<circle cx="12" cy="7" r="4"/><path d="M4 21v-2a8 8 0 0 1 16 0v2z"/>'
  };
  document.querySelectorAll('.nav-link').forEach(link => {
    const section = new URL(link.href).pathname.split('/')[1];
    if (icons[section]) link.insertAdjacentHTML('afterbegin', '<svg aria-hidden="true" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round">' + icons[section] + '</svg>');
    if (path.split('/')[1] === section) { link.classList.add('active'); link.setAttribute('aria-current', 'page'); }
  });
  const toggle = document.querySelector('.navbar-toggler');
  toggle?.addEventListener('click', () => {
    const open = document.querySelector('#mainNav').classList.toggle('is-open');
    toggle.setAttribute('aria-expanded', String(open));
    toggle.setAttribute('aria-label', open ? 'Cerrar navegación' : 'Abrir navegación');
  });
  document.querySelectorAll('.status').forEach(item => {
    if (!['ACTIVO','ACTIVA'].includes(item.textContent.trim().toUpperCase())) item.classList.add('status-muted');
  });
  const descriptions = {'/classes':'Organiza los horarios y las inscripciones del gimnasio.','/members':'Toda la información de tus miembros, en un solo lugar.','/memberships':'Administra los planes y la vigencia de cada membresía.','/payments':'Consulta los pagos y encuentra cada movimiento.','/attendance':'Lleva el control de las visitas al gimnasio.','/employees':'Gestiona la información de tu equipo.','/dashboard':'Una vista general de la actividad de tu gimnasio.','/reports':'Convierte los registros del gimnasio en información útil.'};
  const title = document.querySelector('main h1');
  if (title && descriptions[path]) {
    const subtitle = document.createElement('p');
    subtitle.className = 'page-description';
    subtitle.textContent = descriptions[path];
    title.after(subtitle);
  }
});
