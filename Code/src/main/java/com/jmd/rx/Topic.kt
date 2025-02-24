package com.jmd.rx

enum class Topic {

    APPLICATION_START_FINISH,

    CHANGE_THEME,

    UPDATE_THEME_TEXT,
    UPDATE_UI,
    UPDATE_PROXY_STATUS,

    MAIN_FRAME_SELECTED_INDEX,
    MAIN_FRAME_SHOW,
    MAIN_FRAME_HIDE,
    FLOATING_WINDOW_TOGGLE,
    SET_INTERVAL,
    CLEAR_INTERVAL,

    TASK_STATUS_CURRENT,
    TASK_STATUS_MAP_TYPE,
    TASK_STATUS_LAYERS,
    TASK_STATUS_SAVE_PATH,
    TASK_STATUS_PATH_STYLE,
    TASK_STATUS_IS_COVER_EXIST,
    TASK_STATUS_IMG_TYPE,
    TASK_STATUS_TILE_ALL_COUNT,
    TASK_STATUS_TILE_DOWNLOADED_COUNT,
    TASK_STATUS_PROGRESS,
    TASK_STATUS_ENUM,

    CPU_PERCENTAGE_DRAW_SYSTEM,
    CPU_PERCENTAGE_DRAW_PROCESS,
    CPU_PERCENTAGE_CLEAR,

    RESOURCE_USAGE_THREAD_COUNT,
    RESOURCE_USAGE_DOWNLOAD_SPEED,
    RESOURCE_USAGE_DOWNLOAD_PER_SEC_COUNT,
    RESOURCE_USAGE_SYSTEM_CPU_USAGE,
    RESOURCE_USAGE_PROCESS_CPU_USAGE,
    RESOURCE_USAGE_CLEAR,

    TILE_MERGE_PROCESS_PIXEL_COUNT,
    TILE_MERGE_PROCESS_THREAD,
    TILE_MERGE_PROCESS_PROGRESS,
    TILE_MERGE_PROCESS_CLEAR,

    DOWNLOAD_CONSOLE_PROGRESS,
    DOWNLOAD_CONSOLE_LOG,
    DOWNLOAD_CONSOLE_CLEAR,
    DOWNLOAD_CONFIG_FRAME_ZOOM_SELECTED,
    DOWNLOAD_CONFIG_FRAME_PATH_SELECTED,

    ADD_NEW_LAYER,
    REMOVE_ADDED_LAYER,

    OPEN_TILE_VIEW,

    OPEN_BROWSER_DEV_TOOL,

}