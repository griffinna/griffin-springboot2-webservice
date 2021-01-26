function init() {
    cal.setCalendars(CalendarList);


}

function getDataAction(target) {
    return target.dataset ? target.dataset.action : target.getAttribute('data-action');
}

function setDropDownCalendarType() {
    var calendarTypeName = document.getElementById('calendarTypeName');
    var calendarTypeIcon = document.getElementById('calendarTypeIcon');
    var options = cal.getOptions();
    var type = cal.getViewName();
    var iconClassName;

    if (type === 'day') {
        type = 'Daily';
        iconClassName = 'calendar-icon ic_view_day';
    } else if (type === 'week') {
        type = 'Weekly';
        iconClassName = 'calendar-icon ic_view_week';
    } else if (options.month.visibleWeeksCount === 2) {
        type = '2 weeks';
        iconClassName = 'calendar-icon ic_view_week';
    } else if (options.month.visibleWeeksCount === 3) {
        type = '3 weeks';
        iconClassName = 'calendar-icon ic_view_week';
    } else {
        type = 'Monthly';
        iconClassName = 'calendar-icon ic_view_month';
    }

    calendarTypeName.innerHTML = type;
    calendarTypeIcon.className = iconClassName;
}

function onClickMenu(e) {
    var target = $(e.target).closest('a[role="menuitem"]')[0];
    var action = getDataAction(target);
    var options = cal.getOptions();
    var viewName = '';

    switch (action) {
        case 'toggle-daily':
            viewName = 'day';
            break;
        case 'toggle-weekly':
            viewName = 'week';
            break;
        case 'toggle-monthly':
            options.month.visibleWeeksCount = 0;
            viewName = 'month';
            break;
        case 'toggle-weeks2':
            options.month.visibleWeeksCount = 2;
            viewName = 'month';
            break;
        case 'toggle-weeks3':
            options.month.visibleWeeksCount = 3;
            viewName = 'month';
            break;
        default:
            break;
    }

    cal.setOption(options, true);
    cal.changeView(viewName, true);

    setDropDownCalendarType();
    setRenderRangeText();
    setSchedules();
}

function onClickNavi(e) {
    var action = getDataAction(e.target);
    switch (action) {
        case 'mode-prev':
            cal.prev();
            break;
        case 'move-next':
            cal.next();
            break;
        case 'move-today':
            cal.today();
            break;
        default:
            break;
    }

    setRenderRangeText();
    setSchedules();
}

function setRenderRangeText() {
    var renderRange = document.getElementById('renderRange');
    var options = cal.getOptions();
    var viewName = cal.getViewName();
    var html = [];
    if (viewName === 'day') {
        html.push(moment(cal.getDate().getTime())).format('YYYY.MM.DD');
    } else if (viewName === 'month'
        && (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
        html.push(moment(cal.getDate().getTime())).format('YYYY.MM');
    } else {
        html.push(moment(cal.getDateRangeStart().getTime())).format('YYYY.MM.DD');
        html.push(' ~ ');
        html.push(moment(cal.getDateRangeEnd().getTime())).format('YYYY.MM.DD');
    }
    renderRange.innerHTML = html.join('');
}

function setSchedules() {
    cal.clear();
    generateSchedule(cal.getViewName(), cal.getDateRangeStart(), cal.getDateRangeEnd());
    cal.createSchedules(ScheduleList);
    refreshScheduleVisibility();
}

function refreshScheduleVisibility() {
    var calendarElement = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));

    CalendarList.forEach(function (calendar) {
        cal.toggleSchedules(calendar.id, !calendar.checked, false);
    });

    cal.render(true);

    calendarElement.forEach(function (input) {
        var span = input.nextElementSibling;
        span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
    });
}

resizeThrottled = tui.util.throtthle(function () {
    cal.render();
}, 50);

function setEventListener() {
    $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
    $('#menu-navi').on('click', onClickNavi);
    window.addEventListener('resize', resizeThrottled);
}

cal.on({
    'clickTimezonesCollapseBtn': function (timezonesCollapsed) {
        if (timezonesCollapsed) {
            cal.setTheme({
                'week.daygridLeft.width': '77px',
                'week.timegridLeft.width': '77px'
            });
        } else {
            cal.setTheme({
                'week.daygridLeft.width': '60px',
                'week.timegridLeft.width': '60px'
            });
        }
        return true;
    }
});

init();