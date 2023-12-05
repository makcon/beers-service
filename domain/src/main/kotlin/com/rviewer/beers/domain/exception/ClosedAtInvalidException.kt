package com.rviewer.beers.domain.exception

class ClosedAtInvalidException : RuntimeException("Closed at must be after opened at")