% random beginning:
A=randi(2,5)-ones(5)
% type a number for the times of the iterations
count=input('type: ');

s=size(A);
% animating the iterations
for i = 1:count
    for row = 1:s(1)
        for col = 1:s(2)
            if A(row,col)==1
                plot(col,s(1)-row+1,'s', 'Markersize',50,'MarkerFaceColor','r','MarkerEdgeColor','r');
                hold on
                ani(i)=getframe;
                i=i+1;
            else
                plot(col,s(1)-row+1,'s', 'Markersize',50,'MarkerFaceColor','k','MarkerEdgeColor','k');
                hold on
                ani(i)=getframe;
                i=i+1;
            end
        end
    end
    A
    A=Turn(A);
end

B=IsPeriod([0,1,0;0,1,0;0,1,0]);
