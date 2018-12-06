declare var Tobago: {
    Phase: {
        DOCUMENT_READY,
        WINDOW_LOAD,
        BEFORE_SUBMIT,
        AFTER_UPDATE,
        BEFORE_UNLOAD,
        BEFORE_EXIT
        Order: {
            EARLY,
            NORMAL,
            LATE,
            LATER
        }
    };
    registerListener(
        listener: () => void,
        phase: any,
        order?: any
    ): void
};
