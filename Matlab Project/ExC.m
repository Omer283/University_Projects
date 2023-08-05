boardSize=[5 4]
periBegins = searchPeriods(boardSize);
for i=1:length(periBegins)
    x=periBegins(i);
    numToMat(x,boardSize)
end
amount= length(periBegins)
