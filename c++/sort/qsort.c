#include <stdio.h>

void swap(int *a, int *b)
{ 
  int t=*a; *a=*b; *b=t; 
}
void qsort(int arr[],int l,int r)
{
     int i = l;
     int j = r;
     int key = arr[(i+j)/2];
     while(i < j)
     {
        for(;(i < r)&&(arr[i] < key);i++);
        for(;(j > l)&&(arr[j] > key);j--);
        if (i <= j)
           {
               swap(&arr[i],&arr[j]);
               i++;
               j--;
            }
     }
     if (i < r)
           qsort(arr,i,r);
     if (j > l)
           qsort(arr,l,j);
}

int main()
{
    int i;
    int a[] = {1, 4, 6, 5, 7, 3, 2};
    qsort(a, 0, (sizeof(a)/sizeof(int)) - 1);
    for (i = 0; i < sizeof(a) / sizeof(int); ++i)
    {
        printf("%d ", a[i]);
    }
    printf("\n");

    return 0;
}
